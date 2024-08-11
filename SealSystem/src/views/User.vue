<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入昵称" suffix-icon="el-icon-search" v-model="nickname"></el-input>
      <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
      <el-input style="width: 200px" placeholder="请输入手机" suffix-icon="el-icon-position" class="ml-5" v-model="phone"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-button type="primary" @click="exp" class="ml-5" v-if="user.roleId == 1">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="userId" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名" width="140"></el-table-column>
      <el-table-column prop="roleName" label="角色"></el-table-column>
      <el-table-column prop="college" label="归属"></el-table-column>
      <el-table-column prop="nickname" label="昵称" width="170"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="phone" label="电话" width="100px"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="140px" sortable></el-table-column>
      <el-table-column prop="updateTime" label="修改时间" width="140px"></el-table-column>
      <el-table-column prop="isDelete" label="状态" width="70px" sortable>
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.isDelete != '1'">正常</el-tag>
          <el-tag type="danger" v-if="scope.row.isDelete == '1'">已删除</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作"  width="320" align="center">
        <template slot-scope="scope">
          <el-popconfirm
                  class="ml-5"
                  confirm-button-text='确定'
                  cancel-button-text='我再想想'
                  icon="el-icon-info"
                  icon-color="red"
                  title="您确定重置吗？"
                  @confirm="anew(scope.row.userId)">
            <el-button type="info" slot="reference" v-if="scope.row.isDelete != '1'">密码重置 <i class="el-icon-refresh-right"></i></el-button>
          </el-popconfirm>
          <el-button style="margin-left: 5px" type="success" @click="handleEdit(scope.row)"  v-if="scope.row.isDelete != '1'">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.userId)">
            <el-button type="danger" slot="reference" v-if="scope.row.isDelete != '1'">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @current-change="handleCurrentChange"
          :page-size="pageSize"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%" v-if="user.roleId == '1'">
      <el-form label-width="80px" size="small" :model="form" :rules="rules" ref="pass">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" autocomplete="off" placeholder="用户名一经填写，不可更改！" :disabled="flag"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select clearable v-model="form.roleName" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="归属" prop="college" v-if="this.form.roleName == '普通教师'">
          <el-select clearable v-model="form.college" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roles1" :key="item.name" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="归属" prop="college" v-if="this.form.roleName != '普通教师'">
          <el-input v-model="this.form.roleName" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%" v-if="user.roleId != '1'">
      <el-form label-width="80px" size="small" :model="form" :rules="rules" ref="pass">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" autocomplete="off" placeholder="用户名一经填写，不可更改！" :disabled="flag"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select clearable v-model="form.roleName" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roles2" :key="item.name" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="归属" prop="college" v-if="this.form.roleName == '普通教师'">
          <el-select clearable v-model="form.college" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roles1" :key="item.name" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="归属" prop="college" v-if="this.form.roleName != '普通教师'">
          <el-input v-model="this.form.roleName" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
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
  name: "User",
  data() {
    return {
      flag: true,
      serverIp: serverIp,
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 15,
      nickname: "",
      email: "",
      phone: "",
      address: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {},
      roles: [],
      roles1: [],
      roles2: [],
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 15, message: '长度限制3-15位', trigger: ['blur', 'change'] },
        ],
        roleName: [
          { required: true, message: '请选择权限角色', trigger: 'blur' },
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { min: 11, max: 11, message: '长度应为11位', trigger: ['blur', 'change'] }
        ],
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      if (this.nickname != "" || this.email != "" || this.phone != "") {
        this.pageNum = '1'
      }
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          nickname: this.nickname,
          email: this.email,
          phone: this.phone
        }
      }).then(res => {

        this.tableData = res.data.records
        this.total = res.data.total

      })
      this.request.get("/role").then(res => {
        this.roles = []
        this.roles1 = []
        this.roles2 = []
        res.data.forEach(item => {
          if (item.name != '超级管理员') {
            this.roles.push(item)
          }
          if (item.name != '超级管理员' && item.name != '普通用户' && item.name != '普通教师') {
            this.roles1.push(item)
          }
          if (item.name == '普通用户' || item.name == '普通教师') {
            this.roles2.push(item)
          }
        })
      })
    },
    save() {

      if (this.form.roleName == '普通教师' && this.form.college == undefined) {
        this.$message.error("数据未填写完整！");
      } else {
        this.$refs.pass.validate((valid) => {
          if (valid) {
            let roleInfo = JSON.parse(localStorage.getItem("roleInfo"))
            roleInfo.forEach(item => {
              if (this.form.roleName == item.name) {
                this.form.roleId = item.roleId
              }
            })

            if (this.form.roleName != '普通教师') {
              this.form.college = this.form.roleName
            }

            this.request.post("/user", this.form).then(res => {
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
      }
    },
    handleAdd() {
      this.flag = false
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.flag = true
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(userId) {
      this.request.delete("/user/" + userId).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    anew(userId) {
      this.request.get("/user/resetPwd?userId=" + userId).then(res => {
        if (res.code === '200') {
          this.$message.success("重置成功，重置默认密码为123123，请牢记！")
          this.load()
        } else {
          this.$message.error("重置失败，请联系作者!")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.userId)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/user/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.nickname = ""
      this.email = ""
      this.phone = ""
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
    exp() {
      window.open(`http://${serverIp}:9090/user/export`)
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功!")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
