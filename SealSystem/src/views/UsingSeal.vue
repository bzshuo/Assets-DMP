<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                      v-model="sealName"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>
        <div style="margin: 10px 30px;display: inline-block">
            <el-button type="primary" @click="handleAdd" v-if="user.roleId != '99'">
                新增 <i class="el-icon-circle-plus-outline"></i></el-button>
        </div>
        <el-table :data="tableData"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column type="expand">
                <template slot-scope="props">
                    <el-form label-position="left" inline class="demo-table-expand">
                        <el-form-item label="用章ID" class="el-form-item">
                            <span>{{ props.row.sealId }}</span>
                        </el-form-item>
                        <el-form-item label="用章名称" class="el-form-item">
                            <span>{{ props.row.sealName }}</span>
                        </el-form-item>
                        <el-form-item label="用章类型" class="el-form-item">
                            <span>{{ props.row.type }}</span>
                        </el-form-item>
                        <el-form-item label="用章图片">
                            <img :src="props.row.urlPath" width="60px" height="60px" class="image">
                        </el-form-item>
                        <el-form-item label="状态">
                            <span>{{ props.row.state }}</span>
                        </el-form-item>
                        <el-form-item label="用章人">
                            <span>{{ props.row.staff }}</span>
                        </el-form-item>
                        <el-form-item label="创建时间">
                            <span>{{ props.row.createTime }}</span>
                        </el-form-item>
                    </el-form>
                </template>
            </el-table-column>
            <el-table-column prop="sealId" label="用章ID" sortable ></el-table-column>
            <el-table-column prop="sealName" label="用章名称"></el-table-column>
            <el-table-column prop="type" label="用章类型" ></el-table-column>
            <el-table-column prop="urlPath" label="用章图片" width="200px">
                <template slot-scope="scope">
                    <img :src="scope.row.urlPath" width="60px" height="60px" class="image">
                </template>
            </el-table-column>
            <el-table-column prop="state" label="状态" sortable>
                <template slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.state === '0'">颁发</el-tag>
                    <el-tag type="warning" v-if="scope.row.state === '1'">失效</el-tag>
                </template>
            </el-table-column>

            <el-table-column prop="staff" label="用章人" sortable></el-table-column>
            <el-table-column prop="createTime" label="创建时间" ></el-table-column>

            <el-table-column label="操作" width="200" align="center" fixed="right" v-if="user.roleId != '99'">
                <template slot-scope="scope" >
                    <el-button type="success" @click="handleEdit(scope.row)">编辑 <i
                            class="el-icon-edit"></i></el-button>
                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确定'
                            cancel-button-text='我再想想'
                            icon="el-icon-info"
                            icon-color="red"
                            title="您确定删除吗？"
                            @confirm="del(scope.row.assId)"
                    >
                        <el-button type="danger" slot="reference" >删除 <i
                                class="el-icon-remove-outline"></i></el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <div style="padding: 10px 0">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageNum"
                    :page-size="pageSize"
                    :total="total">
            </el-pagination>
        </div>

        <el-dialog title="用章信息" :visible.sync="dialogFormVisible" width="40%">
            <el-form label-width="100px" size="small" :rules="rules" :model="form" ref="from">
                <el-form-item label="签章名称" prop="sealName">
                    <el-input placeholder="签章名称唯一" v-model="form.sealName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="用章类型" prop="type">
                    <el-select clearable v-model="form.type" placeholder="请选择">
                        <el-option v-for="item in typeList"
                                   :key="item"
                                   :label="item"
                                   :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="状态" prop="state">
                    <el-select clearable v-model="form.state" placeholder="请选择">
                        <el-option v-for="item in sealStateList"
                                   :key="item.key"
                                   :label="item.key"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="用章图片" prop="urlPath">
                    <el-upload
                            v-model="form.urlPath"
                            class="avatar-uploader"
                            :show-file-list="false"
                            list-type="picture-card"
                            :action="'http://' + serverIp +':9090/file/upload/seal'"
                            :on-success="handleAvatarSuccess1"
                    >
                        <img v-if="form.urlPath" :src="form.urlPath" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>

    import {serverIp} from "../../public/config";

    export default {
        name: "UsingSeal",
        data() {
            return {
                serverIp: serverIp,
                form: {},
                tableData: [],
                sealName: '',
                multipleSelection: [],
                typeList: ['公章', '手写'],
                sealStateList: [{
                        key: '颁发',
                        value: '0'
                    },
                    {
                        key:  '失效',
                        value: '1'
                    }],
                pageNum: 1,
                pageSize: 7,
                total: 0,
                dialogFormVisible: false,
                user: sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {},
                exp() {
                    window.open(`http://${serverIp}:9090/usingSeal/export`)
                },
                rules: {
                    sealName: [
                        {required: true, message: '请输入申请标题', trigger: 'blur'},
                        {min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请输入用章·名称', trigger: 'blur'},
                    ],
                    state: [
                        {required: true, message: '请输入申请描述', trigger: 'blur'},
                    ],
                    urlPath: [
                        {required: true, message: '请上传用章图片', trigger: 'blur'}
                    ]
                }
            }
        },
        created() {
            this.load()
        },
        methods: {
            handleAvatarSuccess1(res) {
                if (res.codeMsg === 200) {
                    this.form.urlPath = res.url
                    this.$forceUpdate()
                    this.$message.success("上传成功");
                } else {

                    this.$message.error(res.msg);
                }
            },
            load() {
                this.request.get("/usingSeal/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        sealName: this.sealName
                    }
                }).then(res => {

                    this.tableData = res.data.records
                    this.total = res.data.total

                })
            },
            handleAdd() {
                this.dialogFormVisible = true
                this.form = {}
            },
            handleEdit(row) {
                this.form = JSON.parse(JSON.stringify(row))
                this.dialogFormVisible = true
            },
            del(assId) {
                this.request.delete("/usingSeal/" + assId).then(res => {
                    if (res.code === '200') {
                        this.$message.success("删除成功")
                        this.load()
                    } else {
                        this.$message.error(res.msg)
                    }
                })
            },
            handleSelectionChange(val) {
                console.log(val)
                this.multipleSelection = val
            },
            delBatch() {
                let ids = this.multipleSelection.map(v => v.assId)  // [{}, {}, {}] => [1,2,3]
                this.request.post("/usingSeal/del/batch", ids).then(res => {
                    if (res.code === '200') {
                        this.$message.success("批量删除成功")
                        this.load()
                    } else {
                        this.$message.error("批量删除失败")
                    }
                })
            },
            save() {
                this.$refs['from'].validate((valid) => {
                    if (valid) {  // 表单校验合法
                        // this.form.monthDepRate = "0." + this.form.monthDepRate
                        this.request.post("/usingSeal", this.form).then(res => {
                            if (res.code === '200') {
                                this.$message.success(res.msg)
                                this.dialogFormVisible = false
                                this.load()
                            } else {
                                this.$message.error(res.msg)
                            }
                        })
                    }
                })
            },
            reset() {
                this.sealName = ""
                this.load()
            },
            handleSizeChange(pageSize) {
                console.log(pageSize)
                this.pageSize = pageSize
                this.load()
            },
            handleCurrentChange(pageNum) {
                console.log(pageNum)
                this.pageNum = pageNum
                this.load()
            },
            download(url) {
                window.open(url)
            },
        }
    }
</script>

<style>
    .avatar-uploader {
        text-align: center;
        padding-bottom: 10px;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 138px;
        height: 138px;
        line-height: 138px;
        text-align: center;
    }
    .avatar {
        margin: 3px 0 0 3px;
        width: 140px;
        height: 140px;
        display: block;
    }
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    .demo-table-expand {
        font-size: 0;
    }
    .demo-table-expand label {
        font-weight: bold;
        margin-left: 110px;
        width: 100px;
        color: #99a9bf;
    }
    .demo-table-expand .el-form-item{
        margin-right: 0;
        margin-bottom: 0;
        width: 50%;
    }
</style>
