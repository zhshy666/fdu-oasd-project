<template>
<div>
    <navbar></navbar>
    <el-container>
      <el-main>
        <el-col :span="8" :offset="1">
            <span class="mySpan">{{title}} </span> &nbsp;&nbsp;&nbsp;
            <span style="font-size: small">  @ {{username}}</span>
        </el-col>
        <el-col :span="18" :offset="3">
            <el-divider></el-divider>
            <br>
            <br>
            <el-col :span="12" :offset="offset">
              <el-card shadow="hover" :body-style="{ padding: '0px', height: '400px'}" :style="styleModel">
              <img :src="path" class="image">
              </el-card>
            </el-col>
        </el-col>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import navbar from "../components/Navbar"
import footerbar from "../components/footer"

export default {
    components: {navbar, footerbar},
    name: "ImageDetail",
    data(){
        return{
          title: '',
          username: '',
          path: '',
          imgInfo: {},
          styleModel: {},
          offset: 1,
        };
    },
    methods:{
      getImgInfo () {
        let img = new Image()
        img.src = this.path
        img.onload = () => {
          this.$set(this.imgInfo, 'width', img.width)
          this.$set(this.imgInfo, 'height', img.height)
          this.setImgInfo()
        }
        console.log(this.imgInfo) // 打印图片信息
      },
      setImgInfo(){
        if(this.imgInfo.height > this.imgInfo.width){
            this.styleModel.width=(400/this.imgInfo.height*this.imgInfo.width) + "px"
            this.styleModel.height=400 + "px"
            this.offset = 3;
        }
      },
      errorNotification(){
        this.$notify({
          type:'error',
          dangerouslyUseHTMLString: true,
          title: 'Request error',
          message: '<strong style="color:teal">Requset error, please try again.</strong>'
        });
      },
    },
    created() {
        this.$axios
        .post("/imageDetail",{
            imageId: this.$route.params.imageId
        })
        .then(resp => {
            if(resp.status === 200){
                this.title = resp.data.title;
                this.username = resp.data.username;
                this.path = "/static/travel-images/medium/" + resp.data.path;
                this.getImgInfo();
            } else {
              this.errorNotification();
            }
        })
        .catch(error => {
            console.log(error);
            this.errorNotification();
          });
    }
}
</script>

<style scoped>
.mySpan{
  color: #009688;
  font-size: larger;
  margin-left: 20px;
}
.image {
  width: 100%;
  height: 100%;
  display: block;
}
</style>