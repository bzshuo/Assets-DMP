<template>
    <div>
        <!--    头部-->
        <!--    border-bottom: 1px solid #eee;-->
        <div style="width: 100%; display: flex; height: 60px; line-height: 60px; background-color: #fff">
            <div style="width: 500px; display: flex; padding-left: 30px">
                <div style="width: 60px">
                    <img src="../../assets/ncist.jpg" alt=""
                         style="width: 43px; height: auto; position: relative; top: 10px; right: 0">
                </div>
                <div style="flex: 1; color: #4e6079; font-weight: bold; font-size: 18px">电子签章管理系统</div>
            </div>
            <div style="flex: 1; display: inline-block;">
            </div>
            <div style="width: 200px" @refreshUser="getUser">
                <div v-if="!user.username" style="text-align: right; padding-right: 30px">
                    <el-button
                            style="background: #409EFF; color: #fff; border: none; font-size: 13px;"
                            @click="$router.push('/login')">登录
                    </el-button>
                    <el-button style="border: none; font-size: 13px;"
                               @click="$router.push('/register')">注册
                    </el-button>
                </div>
                <div v-else :user="user">
                    <el-dropdown style="width: 200px; cursor: pointer; text-align: right">
                        <div style="display: inline-block; margin-right: 20px">
                            <span style="font-size: 16px">{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px; font-size: 17px"></i>
                        </div>
                        <el-dropdown-menu slot="dropdown" style="width: 90px; text-align: center">
                            <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                                <router-link to="/home">后台管理</router-link>
                            </el-dropdown-item>
                            <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                                <router-link to="/front/person">个人信息</router-link>
                            </el-dropdown-item>
                            <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                                <router-link to="/front/password">修改密码</router-link>
                            </el-dropdown-item>
                            <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                                <span style="text-decoration: none" @click="logout">退出</span>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </div>
        </div>
        <!--    展示体-->
        <div style="margin: 0 0; height: 93vh; background-color: #f5f5f5">
            <div style="width: 1300px; margin: 0 auto; text-align: center;">
                <router-view/>
            </div>
        </div>
    </div>
</template>

<script>
    import Header from "../../components/Header";
    export default {
        name: "Front",
        components: {Header},
        data() {
            return {
                user: sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : ''
            }
        },
        created() {
            this.getUser()
        },
        methods: {
            logout() {
                this.$store.commit("logout")
                sessionStorage.clear()
                this.$message.success("退出成功")
            },
            open() {
            },
            getUser() {
                let username = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")).username : ""
                if (username) {
                    // 从后台获取User数据
                    this.request.get("/user/username/" + username).then(res => {
                        // 重新赋值后台的最新User数据
                        this.user = res.data
                    })
                }
            }
        }
    }
</script>

<style>
    .item {
        display: inline-block;
        width: 80px;
        height: 59px;

        text-align: center;
        cursor: pointer
    }

    .item a {
        color: #000;
        font-size: 13px
    }

    /*.item:hover{*/
    /*  background-color: #fafafb;*/
    /*}*/
    a:hover {
        color: #e96a0d;
    }

    .wrapper {
        height: 100vh;
        background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
        overflow: hidden;
    }
</style>
