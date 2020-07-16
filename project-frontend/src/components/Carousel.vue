<template>
<el-container>
    <el-main>
        <el-col :span="20" :offset="2">
            <el-carousel :interval="4000" type="card" height="360px">
                <el-carousel-item v-for="item in images" :key="item">
                <!-- <h3 class="medium">{{ item }}</h3> -->
                <img 
                :src="baseURL + item['path']" 
                class="myImg" /><br><br>
                <i class="el-icon-picture-outline-round"></i>
                <span>{{item['title']}} &nbsp;&nbsp;&nbsp;</span>
                <i class="el-icon-star-off"></i>
                <span v-if="large(item)">99+</span>
                <span v-else>{{item['heat']}}</span>
                </el-carousel-item>
            </el-carousel>
        </el-col>
    </el-main>
</el-container>
</template>

<script>
export default {
    name: "carousel",
    data(){
        return{
            images: [],
            baseURL: '/static/travel-images/small/',
        }
    },
    computed: {
    },
    methods: {
        large(item){
            if(item['heat'] <= 99)
                return false;
            return true;
        }
    },
    created(){
        this.$axios
        .get("/getPopularImages",{})
        .then(resp => {
            if (resp.status === 200) {
                this.images = resp.data;
            } else {
            this.$message.error("Request Error.")
            }
        })
        .catch(error =>{
            console.log(error);
        })
    }
}
</script>

<style>
.myImg{
    height: 320px;
    width: 480px;
}
</style>