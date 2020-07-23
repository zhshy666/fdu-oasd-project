<template>
<div>
    <navbar></navbar>
    <el-container>
      <el-main>
        <el-col :span="9" :offset="titleOffset">
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
                    <span class="cardSpan">Image info</span> &nbsp; &nbsp;
                    <el-tooltip v-if="isAuthor" class="item" effect="dark" content="Delete" placement="top">
                        <el-link :underline="false" v-on:click="deleteImg(image)"><i class="el-icon-delete">&nbsp;&nbsp;&nbsp;</i></el-link>
                    </el-tooltip>
                    <el-tooltip v-if="isAuthor" class="item" effect="dark" content="Modify" placement="top">
                        <el-link :underline="false" v-on:click="routeTo(image)"><i class="el-icon-edit"></i>&nbsp;&nbsp;&nbsp;</el-link>
                    </el-tooltip>

                    <el-tooltip v-if="isFavor" class="item" effect="dark" content="Remove from my favors" placement="top">
                        <el-link :underline="false" v-on:click="removeFavor(image)"><i class="el-icon-star-on"></i></el-link>
                    </el-tooltip>
                    <el-tooltip v-if="isNotFavor" class="item" effect="dark" content="Add to my favors" placement="top">
                        <el-link :underline="false" v-on:click="addFavor(image)"><i class="el-icon-star-off"></i></el-link>
                    </el-tooltip>

                  </div>
                  <div>
                    <i class="myTitle"> &nbsp; Author &nbsp;</i>
                    <div class="myInfo">{{image.author}}</div>
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
    inject: ["reload"],
    name: "ImageDetail",
    data(){
        return{
          image:[],
          path: '',
          country: '',
          city: '',
          isAuthor: false,
          isFavor: false,
          isNotFavor: true,
          imgInfo: {},
          styleModel: {},
          offset: 1,
          titleOffset: 0,
          span: 12
        };
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
              if(this.image.title.length > 5){
                this.titleOffset = 1;
              }

              // not log in
              if(!this.$store.state.token){
                this.isNotFavor = false;
              }
              if(resp.data.favor === true){
                  this.isFavor = true;
                  this.isNotFavor = false;
                }
              if(this.$store.state.token && this.$store.state.cur_user === this.image.username){
                this.isAuthor = true;
              }
            } else {
              this.errorNotification();
            }
        })
        .catch(error => {
            console.log(error);
            this.errorNotification();
          });
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
        console.log(this.imgInfo)
      },
      routeTo(image){
        this.$router.push({name: 'Upload', params: {imageId: image.imageId}})
      },
      setImgInfo(){
        if(this.imgInfo.height > this.imgInfo.width){
            this.styleModel.width=(400/this.imgInfo.height*this.imgInfo.width) + "px"
            this.styleModel.height=400 + "px"
            this.offset = 3;
            this.span = 8;
        }
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
                            this.$router.replace("/");
                            this.$notify({
                                type: "success",
                                dangerouslyUseHTMLString: true,
                                title: "Delete success",
                                message:
                                    "<strong style='color:teal'>Delete successfully.</strong>"
                            });
                        }
                    })
                    .catch(error => {
                        console.log(error);
                        this.errorNotification();
                    });
            })
      },
      removeFavor(image){
        this.$confirm("Are you sure to remove this image from your favors?", "Remove confirm", {
                confirmButtonText: "Yes",
                cancelButtonText: "No"
            })
            .then(()=>{
              this.$axios
                .post("/removeFavor",{
                  imageId: image.imageId
                })
                .then(resp => {
                  if(resp.status === 200){
                      this.reload();
                      this.$notify({
                        type: "success",
                        dangerouslyUseHTMLString: true,
                        title: "Remove success",
                        message:
                          "<strong style='color:teal'>Remove successfully.</strong>"
                      });
                  }
                })
            })
      },
      addFavor(image){
          this.$axios
            .post("/addFavor",{
                  imageId: image.imageId
            })
            .then(resp => {
              if(resp.status === 200){
                  this.reload();
                  this.$notify({
                    type: "success",
                    dangerouslyUseHTMLString: true,
                    title: "Add success",
                    message:
                      "<strong style='color:teal'>Add favor successfully.</strong>"
                  });
                }
            })
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
}
</script>

<style scoped>
.mySpan{
  color: #009688;
  font-size: larger;
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