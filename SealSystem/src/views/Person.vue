<template>
  <el-card style="width: 500px; margin: 20px auto">
    <el-form label-width="80px" size="small" :model="form" :rules="rules" ref="pass">
<!--      <el-upload-->
<!--          class="avatar-uploader"-->
<!--          :action="'http://' + serverIp +':9090/file/upload'"-->
<!--          :show-file-list="false"-->
<!--          :on-success="handleAvatarSuccess"-->
<!--      >-->
<!--        <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar" style="margin: -2px;">-->
<!--        <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
<!--      </el-upload>-->

      <el-form-item label="用户名">
        <el-input v-model="form.username" disabled autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="form.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="当前权限" style="text-align: left">
        <el-divider direction="vertical"></el-divider>
        <span>{{roleName}}</span>
        <el-divider direction="vertical"></el-divider>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "Person",
  data() {
    return {
      serverIp: serverIp,
      form: {},
      user: sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {},
      roleName: '',
      rules: {
        nickname: [
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 1, max: 8, message: '长度限制8位', trigger: ['blur', 'change'] },
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { min: 11, max: 11, message: '长度应为11位', trigger: 'blur' }
        ],
      }
    }
  },
  created() {
    this.getUser().then(res => {
      console.log(res)
      this.form = res
      let roleInfo = JSON.parse(localStorage.getItem("roleInfo"));
      roleInfo.forEach(item => {
        if (this.form.roleId == item.roleId) {
          this.roleName = item.name
          console.log(this.roleName)
        }
      })
    })
  },
  methods: {
    async getUser() {
      return (await this.request.get("/user/username/" + this.user.username)).data
    },
    save() {
      this.$refs.pass.validate((valid) => {
        if (valid) {
          this.request.post("/user", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("保存成功")

              // 触发父级更新User的方法
              this.$emit("refreshUser")

              // 更新浏览器存储的用户信息
              this.getUser().then(res => {
                res.token = JSON.parse(sessionStorage.getItem("user")).token
                localStorage.setItem("user", JSON.stringify(res))
              })
              this.$router.push("/home")

            } else {
              this.$message.error("保存失败")

            }
          })
        }
      })
    },
    handleAvatarSuccess(res) {
      this.form.avatarUrl = res
    }
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
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
  width: 138px;
  height: 138px;
  display: block;
}
</style>
