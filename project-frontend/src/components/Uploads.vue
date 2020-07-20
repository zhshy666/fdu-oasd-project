<template>
    <el-container>
        <el-main>
            <el-col :span="24"  v-if="hasUploads">
                <div class=" layui-row">
                    <div class=" layui-col-md3 card layui-col-md-offset3" 
                    v-for="(image,i) in images.slice((currentPage-1)*6,currentPage*6)" :key="i">
                        <router-link
                        :to="'imageDetail/'+image.imageId">
                        <img :src="baseURL + image.path" class=" newImg" />
                        </router-link>
                        <div>
                            <br>
                            <i class="myTitle"> &nbsp; Author &nbsp;</i>
                            <div class="myInfo">{{image.author}}</div>
                            <br>
                            <i class="myTitle"> &nbsp; Content &nbsp;</i>
                            <div class="myInfo">{{image.content}}</div>
                            <br>
                            <i class="myTitle"> &nbsp; Time &nbsp;</i>
                            <div class="myInfo">{{image.releasedTime}}</div>
                            <br>
                            <i class="myTitle"> &nbsp; Heat &nbsp;</i>
                            <div class="myInfo">{{image.heat}}</div>
                            <br>
                        </div>
                        <div>
                            <el-tooltip class="item" effect="dark" content="Delete" placement="top">
                                <el-link v-on:click="deleteImg(image)"><i class="el-icon-delete">&nbsp;&nbsp;&nbsp;</i></el-link>
                            </el-tooltip>
                            <el-tooltip class="item" effect="dark" content="Modify" placement="top">
                                <el-link v-on:click="routeTo(image)"><i class="el-icon-edit"></i></el-link>
                            </el-tooltip>
                        </div>
                        <br>
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
            <el-col :span="24" v-else>
                No uploads. Click <router-link :to="'upload'">here</router-link> to post one.
            </el-col>
        </el-main>
    </el-container>
</template>

<script>
import uploadImg from "../pages/Upload"

export default {
    name: "uploads",
    inject: ["reload"],
    data(){
        return{
            images: [],
            baseURL: '/static/travel-images/medium/',
            total: 0,
            currentPage: 1,
            hasUploads: false,
            noUploads: true,
        }
    },
    created() {
        this.$axios
        .get("/getUploads",{})
        .then(resp => {
            if (resp.status === 200) {
                this.images = resp.data;
                this.total = this.images.length;
                if(this.images.length > 0){
                    this.hasUploads = true;
                    this.noUploads = false;
                }
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
        deleteImg(image){
            this.$confirm("Are you sure to delete this image?", "Delete confirm", {
                confirmButtonText: "Yes",
                cancelButtonText: "No"
            })
            .then(()=>{
                this.$axios
                    .post("/deleteImg", {
                        imageId: image.imageId,
                        url: image.path
                    })
                    .then(resp => {
                        if(resp.status === 200){
                            this.reload();
                            this.$notify({
                                type: "success",
                                dangerouslyUseHTMLString: true,
                                title: "Delete success",
                                message:
                                    "<strong style='color:teal'>Delete successfully!</strong>"
                            });
                        }
                    })
                    .catch(error => {
                        console.log(error);
                        this.errorNotification();
                    });
            })
        },
        routeTo(image){
            this.$router.push({name: 'Upload', params: {imageId: image.imageId}})
        },
        errorNotification(){
            this.$notify({
            type:'error',
            dangerouslyUseHTMLString: true,
            title: 'Request error',
            message: '<strong style="color:teal">Requset error, please try again.</strong>'
            });
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
.myTitle{
  color: #009688;
  font-size: small;
  font-style: normal;
  font-weight: bold;
  margin-left: -50%;
}
.myInfo{
  font-size: small;
  margin-left: 60px;
  margin-top: -16px;
}
.card:hover {
    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
}
img {
    border-radius: 5px 5px 0 0;
}
</style>