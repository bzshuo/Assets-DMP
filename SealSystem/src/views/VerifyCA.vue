<template>
    <div style="width: 600px; margin: 50px 0 0 300px">
        <h2>签章验证门户</h2><br/><br/><br/>
        <el-form :model="formData" :rules="rules" ref="formData" label-width="140px" class="demo-ruleForm">
            <el-form-item label="验证签章标题" prop="sendId">
                <el-select clearable v-model="formData.sendId" placeholder="请选择">
                    <el-option v-for="item in sendList"
                               :key="item.sendTitle"
                               :label="item.sendTitle"
                               :value="item.sendId">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="申请验证文件" prop="file">
                <el-upload
                        class="upload-demo"
                        drag
                        :action="'http://' + serverIp +':9090/file/verify/pdf'"
                        :on-exceed="handleExceed"
                        :on-success="handleAvatarSuccess"
                        v-model="formData.file"
                        :limit=1
                        accept="pdf"
                        multiple>
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip" style="color: red">只能上传pdf文件</div>
                </el-upload>
            </el-form-item><br/>


            <el-form-item>
                <el-button type="primary" @click="submit">提交验证</el-button>
            </el-form-item>
        </el-form>

    </div>
</template>

<script>

    import {serverIp} from "../../public/config";

    export default {
        name: "VerifyCA",
        data() {
            return {
                serverIp: serverIp,
                formData: {},
                sendList: [],
                rules: {
                    sendId: [
                        {required: true, message: '请选择验证签章标题', trigger: 'blur'},
                    ],
                    file: [
                        {required: true, message: '请上传申请验证PDF文件', trigger: 'blur'},
                    ],
                }
            };
        },
        created() {
            this.request.get("/sendSeal/sendList").then(res => {
                if (res.code === '200') {
                    this.sendList = []
                    res.data.forEach(item => {
                        this.sendList.push(item)
                    })
                }
            })
        },
        methods: {
            handleExceed(files, fileList) {
                this.$message.warning(`当前限制选择上传 1 个文件，请移除当前列表文件`);
            },
            handleAvatarSuccess(res) {
                if (res.codeMsg === 200) {
                    this.formData.fileName = res.fileName
                    this.$forceUpdate()
                    this.$message.success("上传成功");
                } else {
                    this.$message.error(res.msg);
                    this.$refs.upload.abort();
                }
            },
            submit() {
                this.$refs['formData'].validate((valid) => {
                    if (valid) {  // 表单校验合法
                        this.request.get("/sendSeal/verifyCA", {
                            params: {
                                fileName: this.formData.fileName,
                                sendId: this.formData.sendId
                            }
                        }).then(res => {
                            if (res.code === '200') {
                                this.$message.success(res.msg)
                                this.load()
                            } else {
                                this.$message.error(res.msg)
                            }
                        })
                    }
                })
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
