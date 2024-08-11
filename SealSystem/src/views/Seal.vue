<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                      v-model="sealName"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>
        <div style="margin: 10px 30px;display: inline-block">
            <el-button type="primary" @click="handleAdd">
                新增 <i class="el-icon-circle-plus-outline"></i></el-button>
        </div>
        <el-table :data="tableData"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="sendId" label="申请ID" sortable ></el-table-column>
            <el-table-column prop="userName" label="申请签章用户"></el-table-column>
            <el-table-column prop="sealName" label="用章名称" ></el-table-column>
            <el-table-column label="PDF文件" width="200" align="center" >
                <template slot-scope="scope">
                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确定'
                            cancel-button-text='我再想想'
                            icon="el-icon-info"
                            icon-color="red"
                            title="确定下载吗"
                            @confirm="download(scope.row.originFileUrlUUID)"
                    >
                        <el-button type="success" slot="reference" >下载</el-button>
                    </el-popconfirm>
<!--                    <el-link href="https://element.eleme.io" target="_blank" label="默认链接"></el-link>-->
                </template>
            </el-table-column>
            <el-table-column prop="urlPath" label="用章图片" width="200px">
                <template slot-scope="scope">
                    <img :src="scope.row.urlPath" width="60px" height="60px" class="image">
                </template>
            </el-table-column>
            <el-table-column prop="remark" label="申请描述" ></el-table-column>
            <el-table-column prop="state" label="状态" sortable>
                <template slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.state === '0'">初始</el-tag>
                    <el-tag type="warning" v-if="scope.row.state === '1'">审批</el-tag>
                    <el-tag type="warning" v-if="scope.row.state === '2'">加印完成</el-tag>
                    <el-tag type="warning" v-if="scope.row.state === '3'">作废</el-tag>
                </template>
            </el-table-column>

            <el-table-column prop="createTime" label="创建时间" ></el-table-column>
            <el-table-column prop="approver" label="审批人" ></el-table-column>
            <el-table-column prop="updateTime" label="审批时间" ></el-table-column>
            <el-table-column prop="over" label="用印人" ></el-table-column>
            <el-table-column prop="overTime" label="用印时间" ></el-table-column>

            <el-table-column label="操作" width="200" align="center" fixed="right">
                <template slot-scope="scope">
                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确定'
                            cancel-button-text='我再想想'
                            icon="el-icon-info"
                            icon-color="red"
                            title="您确定作废吗？"
                            @confirm="del(scope.row.sendId)"
                    >
                        <el-button type="danger" slot="reference" v-if="scope.row.state !== '3'">作废 <i
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

        <el-dialog title="申请签章信息" :visible.sync="dialogFormVisible" width="40%">
            <el-form label-width="100px" size="small" :rules="rules" :model="form" ref="from">

                <el-form-item label="用章名称" prop="sealId">
                    <el-select clearable v-model="form.sealId" placeholder="请选择">
                        <el-option v-for="item in usingsealList"
                                   :key="item.sealName"
                                   :label="item.sealName"
                                   :value="item.sealId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="申请加印文件" prop="originFileUrl">
                    <el-upload
                            class="upload-demo"
                            drag
                            :action="'http://' + serverIp +':9090/file/upload/pdf'"
                            :on-exceed="handleExceed"
                            :on-success="handleAvatarSuccess"
                            :limit="1"
                            v-model="form.originFileUrl"
                            accept="pdf"
                            multiple>
                        <i class="el-icon-upload"></i>
                        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                        <div class="el-upload__tip" slot="tip" style="color: red">只能上传pdf文件</div>
                    </el-upload>
                </el-form-item>
                <el-form-item label="申请描述" prop="remark">
                    <el-input
                            type="textarea"
                            :rows="2"
                            placeholder="请输入内容"
                            v-model="form.remark">
                    </el-input>
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
        name: "Seal",
        data() {
            return {
                radio: '1',
                serverIp: serverIp,
                form: {},
                tableData: [],
                sealName: '',
                multipleSelection: [],
                usingsealList: [],
                pageNum: 1,
                pageSize: 7,
                total: 0,
                dialogFormVisible: false,
                user: sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {},
                exp() {
                    window.open(`http://${serverIp}:9090/usingSeal/export`)
                },
                rules: {
                }
            }
        },
        created() {
            this.load()
            this.request.get("/usingSeal/sealList").then(res => {
                if (res.code === '200') {
                    this.usingsealList = []
                    res.data.forEach(item => {
                        this.usingsealList.push(item)
                    })
                }
            })
        },
        methods: {
            handleExceed(files, fileList) {
                this.$message.warning(`当前限制选择上传 1 个文件，本次选择了 ${files.length} 个文件`);
            },
            handleAvatarSuccess(res) {
                if (res.codeMsg === 200) {
                    this.form.originFileUrl = res.originFileUrl
                    this.form.fileSha = res.fileSha
                    this.$forceUpdate()
                    this.$message.success(res.msg);
                } else {
                    this.$message.error(res.msg);
                    this.$refs.upload.abort();
                }
            },
            load() {
                console.log(this.radio)
                this.request.get("/sendSeal/page", {
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
                this.request.delete("/sendSeal/" + assId).then(res => {
                    if (res.code === '200') {
                        this.$message.success("作废成功")
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
                this.request.post("/sendSeal/del/batch", ids).then(res => {
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
                        this.request.post("/sendSeal", this.form).then(res => {
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
                this.pageSize = pageSize
                this.load()
            },
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum
                this.load()
            },
            download(url) {
                if (null == url || '' == url) {
                    this.$message.warning("下载文件未找到")
                    return;
                }
                window.open(`http://${serverIp}:9090/file/pdfOld/` + url)
            },
        }
    }
</script>

<style>
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
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
