<template xmlns:https="http://www.w3.org/1999/xhtml">

    <div>
        <div style="padding-bottom: 20px; display: inline-block;
        /*background-image: url('https://www.drmfab.cn/static/index/images/software/mimaxue.jpg');*/
        /*width: 100ch; height: 100ch;*/

        ">
            <b>欢迎你！<span style="color: red">{{user.role}}</span> - {{ user.nickname }}</b>
        </div>

    </div>
</template>

<script>

    export default {
        name: "Home",
        data() {
            return {
                dates: [],
                dateValue: '',
                user: sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
            }
        },
        created() {
            this.request.get("/echarts/getDateInfo").then(res => {
                if (res.code === '200') {
                    this.dates = res.data
                }
            })
        },
        mounted() {  // 页面元素渲染之后再触发
        },
        methods: {}
    }
</script>

<style scoped>
    .container {
        display: inline-block;
        margin-left: 100px;
    }
</style>
