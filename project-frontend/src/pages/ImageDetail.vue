<template>
<div>
    <navbar></navbar>
    <el-container>
      <el-main>
        <el-col :span="9" :offset="1">
            <span class="mySpan">{{image.title}} </span> &nbsp;&nbsp;&nbsp;
            <span style="font-size: small">  @ {{image.username}}</span>
        </el-col>
        <el-col :span="18" :offset="3">
            <el-divider></el-divider>
            <br>
            <br>
            <el-col :span="span" :offset="offset">
              <el-card shadow="hover" :body-style="{ padding: '0px', height: '400px'}" :style="styleModel">
              <img :src="path" class="image">
              </el-card>
            </el-col>
            <el-col :span="8" :offset="2">
                <el-card class="box-card cardSpan">
                  <div slot="header">
                    <span class="cardSpan">Image info</span>
                  </div>
                  <div>
                    <i class="myTitle"> &nbsp; Author &nbsp;</i>
                    <div class="myInfo">{{image.username}}</div>
                    <br>
                    <i class="myTitle"> &nbsp; Title &nbsp;</i>
                    <div class="myInfo">{{image.title}}</div>
                    <br>
                    <i class="myTitle"> &nbsp; Content &nbsp;</i>
                    <div class="myInfo">{{image.content}}</div>
                    <br>
                    <i class="myTitle"> &nbsp; Description &nbsp;</i>
                    <div class="myInfo">{{image.description}}</div>
                    <br>
                    <i class="myTitle"> &nbsp; Heat &nbsp;</i>
                    <div class="myInfo">{{image.heat}}</div>
                    <br>
                    <i class="myTitle"> &nbsp; Country &nbsp;</i>
                    <div class="myInfo">{{country}}</div>
                    <br>
                    <i class="myTitle"> &nbsp; City &nbsp;</i>
                    <div class="myInfo">{{city}}</div>
                    <br>
                    <i class="myTitle"> &nbsp; Released On &nbsp;</i>
                    <div class="myInfo">{{image.releasedTime}}</div>
                    <br>
                  </div>
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
          image:[],
          path: '',
          country: '',
          city: '',
          imgInfo: {},
          styleModel: {},
          offset: 1,
          span: 12
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
            this.span = 8;
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
              this.image = resp.data.image;
              this.country = resp.data.country;
              this.city = resp.data.city;
              this.path = "/static/travel-images/medium/" + resp.data.image.path;
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
.cardSpan{
  font-size: medium;
  text-align: left;
}
.myTitle{
  color: #009688;
  font-size: small;
  font-style: normal;
  font-weight: bold;
}
.myInfo{
  font-size: small;
  margin-left: 100px;
  margin-top: -15px;
}

</style>