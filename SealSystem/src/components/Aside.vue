<template>
  <el-menu :default-openeds="opens" style="min-height: 100%; overflow-x: hidden"
           background-color="rgb(48, 65, 86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           :default-active='$route.path' :router='true'
  >
    <div style="height: 60px; line-height: 60px; text-align: center">
      <div style="width: 60px">
        <img src="../assets/ncist.jpg" alt=""
             style="width: 43px; height: auto; position: relative; top: 10px; right: 0; margin-left: 70px">
      </div>
     <b style="color: white; margin-left: 100px" v-show="logoTextShow">电子签章管理系统</b>-->
    </div>
    <div v-for="item in menus" :key="item.id">
      <div v-if="item.path && item.path != '/404'">
        <el-menu-item :index="item.path">
          <i :class="item.icon"></i>
          <span slot="title" style="">{{ item.name }}</span>
        </el-menu-item>
      </div>
    </div>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
  data() {
    return {
      menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
      opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : []
    }
  },
}
</script>

<style>
.el-menu-item.is-active {
  background-color: rgb(38, 52, 69) !important;
}
.el-menu-item:hover {
  background-color: rgb(38, 52, 69) !important;
}

.el-submenu__title:hover {
  background-color: rgb(38, 52, 69) !important;
}
/*解决收缩菜单文字不消失问题*/
.el-menu--collapse span {
  visibility: hidden;
}
</style>
