<template>
  <div>
    <br>
    <div style="margin: 0 0">
      <el-carousel height="500px" :interval="10000" style="margin-top: 10px">
        <el-carousel-item v-for="item in imgs" :key="item">
          <a href="https://www.ncist.edu.cn/Home/"><img :src="item" alt="" style="width: 100%"></a>
        </el-carousel-item>
      </el-carousel>
    </div>

      <div style="margin: 30px 0">
          <p style="display: inline-block">应急管理大学(筹) | 华北科技学院是<h3 style="display: inline-block">应急管理部</h3>直属高校，前身是1950年创建的中央燃料工业部干部学校；</p>
          <p style="margin: 10px;">学校把服务支撑大国应急管理事业作为第一面向，坚持立足应急管理、面向公共安全、服务经济社会，着力打造服务应急管理事业需要的人才培养高地、科技创新高地、教育培训高地</p>
          <p>逐步形成了以本科教育为主，研究生教育、留学生教育、成人教育和短期培训多层次、较完整的人才培养体系。</p>
      </div>
  </div>
</template>

<script>
export default {
  name: "FrontHome",
  data() {
    return {
      imgs: [
              'https://pic2.zhimg.com/v2-2bc7c17c519e672ee114627370b6764d_1200x500.jpg',
          'https://img.pc841.com/2015/1128/20151128083234260.jpg'
      ],
      files: []
    }
  },
  created() {
      this.request.get("/role").then(res => {
          localStorage.setItem("roleInfo", JSON.stringify(res.data))
      })
  },
  methods: {
    load(){
        location.reload();
    },

  }
}
</script>

<style>
  .footer-style {
      display: inline-block;
      margin: 5px 40px;
      font-weight: bold;
      font-size: 13px!important;
  }
</style>

