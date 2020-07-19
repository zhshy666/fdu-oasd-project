<template>
    <el-container>
        <el-main>
            <el-col :span="24" >
                <div class=" layui-row">
                    <div class=" layui-col-md3 card layui-col-md-offset3" 
                    v-for="(image,i) in images.slice((currentPage-1)*6,currentPage*6)" :key="i">
                        <router-link
                        :to="'imageDetail/'+image.imageId">
                        <img :src="baseURL + image.path" class=" newImg" />
                            <div class="container">
                                <br>
                                <p><i class="el-icon-user"></i>&nbsp;&nbsp; {{image.username}}</p>
                                <br>
                                <p><i class="el-icon-camera"></i>&nbsp;&nbsp; {{image.content}}</p>
                                <br>
                                <p><i class="layui-icon layui-icon-time"></i>&nbsp;&nbsp; {{image.releasedTime}}</p>
                                <br>
                            </div>
                        </router-link>
                    </div>
                </div>
                <div class="block">
                    <br>
                    <br>
                    <el-pagination
                        layout="prev, pager, next"
                        @current-change="handleCurrentChange"
                        :current-page="currentPage"
                        :page-size="6"
                        :total="total">
                    </el-pagination>
                </div>
            </el-col>
        </el-main>
    </el-container>
</template>

<script>
export default {
    name: "uploads",
    data(){
        return{
            images: [],
            baseURL: '/static/travel-images/medium/',
            total: 0,
            currentPage: 1,
        }
    },
    created() {
        this.$axios
        .get("/getNewestImages",{})
        .then(resp => {
            if (resp.status === 200) {
                this.images = resp.data;
            } else {
                this.$notify({
                    type:'error',
                    dangerouslyUseHTMLString: true,
                    title: 'Request error',
                    message: '<strong style="color:teal">Requset error, please try again.</strong>'
                });
            }
        })
        .catch(error =>{
            console.log(error);
        })
    },
    methods: {
        handleCurrentChange(val){
          this.currentPage = val;
      },
    }
}
</script>

<style scoped>
.newImg{
    height: 210px;
    width: 100%;
}
.mySpan{
    color: #009688;
    font-size: larger;
    margin-left: 20px;
}
.card {
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
    transition: 0.3s;
    border-radius: 5px;
    margin: 30px 20px 20px 60px;
}

.card:hover {
    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
}

img {
    border-radius: 5px 5px 0 0;
}

.container {
    padding: 2px 16px;
}
</style>