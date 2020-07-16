<template>
    <el-container>
        <el-main>
            <el-col :span="18" :offset="3">
                <el-divider></el-divider>
                <p style="font-size:small">Created by zsy with 
                    <a href="https://cn.vuejs.org/" target="blank">vue.js</a>
                     and <a href="https://spring.io/projects/spring-boot" target="blank">springboot</a>.
                </p>
                <br>
            </el-col>
        </el-main>
    </el-container>
</template>

<script>
export default {
    name: "footerbar",
    data() {
        return {}
    },
    methods: {
        goToVue(){
            this.$router.replace("https://cn.vuejs.org/");
        }
    }
}
</script>

<style scoped>

</style>