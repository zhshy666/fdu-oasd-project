<template>
    <el-container>
        <el-main>
            <el-col :span="2" :offset="22" v-if="hasAuthority">
                <el-tooltip 
                effect="dark" 
                :content="'Current status: ' + value" 
                placement="top">
                    <el-switch
                    active-value="public"
                    inactive-value="private"
                    v-model="value"
                    active-color="#13ce66"
                    inactive-color="gray"
                    @change="changeStatus">
                    </el-switch>
                </el-tooltip>
            </el-col>
            <el-col :span="24"  v-if="(hasFavors && hasAuthority) || (hasFavors && !hasAuthority && this.value === 'public')">
                <div class=" layui-row">
                    <div class=" layui-col-md3 card layui-col-md-offset3" 
                    v-for="(image,i) in images.slice((currentPage-1)*6,currentPage*6)" :key="i">
                        <router-link
                        v-if="hasAuthority"
                        :to="'imageDetail/'+image.imageId">
                        <img :src="baseURL + image.path" class="newImg" />
                        </router-link>
                        <router-link
                        v-else
                        :to="'../imageDetail/'+image.imageId">
                        <img :src="baseURL + image.path" class="newImg" />
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
                        <div v-if="hasAuthority">
                            <el-tooltip class="item" effect="dark" content="Remove from my favors" placement="top">
                                <el-link :underline="false" v-on:click="removeFavor(image)"><i class="el-icon-delete"></i></el-link>
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
            <el-col :span="24" v-if="!hasFavors && hasAuthority">
                No favors. Click <router-link :to="'/'">here</router-link> to view images uploaded by other users.
            </el-col>
            <el-col :span="24" v-if="!hasFavors && !hasAuthority && this.value === 'public'">
                No favors.
            </el-col>
            <el-col :span="24" v-if="!hasAuthority && this.value === 'private'">
                The user has made his favors private. You don't have the authority to view.
            </el-col>
        </el-main>
    </el-container>
</template>

<script>
export default {
    name: "favors",
    inject: ["reload"],
    data() {
        return{
            hasFavors: false,
            noFavors: true,
            images: [],
            baseURL: '/static/travel-images/medium/',
            total: 0,
            currentPage: 1,
            username: '',
            hasAuthority: false,
            value: 'private'
        }
    },
    created() {
        if(this.$route.params.username){
            this.username = this.$route.params.username;
        }
        else{
            this.username = this.$store.state.cur_user;
            this.hasAuthority = true;
        }
        this.$axios
        .post("/getFavors",{
            username: this.username
        })
        .then(resp => {
            if (resp.status === 200) {
                this.images = resp.data.images;
                if(resp.data.isPublic === 0){
                    this.value = 'private';
                }else{
                    this.value = 'public';
                }
                console.log(this.value);
                this.total = this.images.length;
                if(this.images.length > 0){
                    this.hasFavors = true;
                    this.noFavors = false;
                }
            } else {
                this.errorNotification();
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
        changeStatus(){
                this.$axios
                    .post("/changeStatusOfFavors", {
                        isPublic: this.value
                    })
                    .then(resp => {
                        if(resp.status === 200){
                            this.reload();
                            this.$message({
                                type: "success",
                                message: 'Change status successfully !',
                            });
                        }else{
                            this.errorNotification();
                        }
                    })
                    .catch(error => {
                        console.log(error);
                        this.errorNotification();
                    });
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
                      this.$message('Remove successfully.');
                  }
                })
            })
        },
        errorNotification(){
            this.$message.error('Requset error, please try again');
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
.tip{
    font-style: normal;
    font-size: small;
}
</style>