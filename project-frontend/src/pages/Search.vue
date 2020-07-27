<template>
<div>
    <navbar></navbar>
    <el-container>
      <el-main>
        <el-col :span="18" :offset="3">
            <el-form
              status-icon
              :model="searchForm"
              ref="searchForm"
              label-position="left"
              label-width="0px"
              v-loading="loading"
              :inline="true"
            >
                <el-form-item prop="input">
                    <el-input
                        type="text"
                        v-model="searchForm.input"
                        auto-complete="off"
                        placeholder="Search for images"
                        prefix-icon="el-icon-search"
                        style="width: 450px"
                    ></el-input>
                </el-form-item>

                <el-form-item prop="scope" >
                    <el-select 
                    v-model="searchForm.scope" 
                    placeholder="Select scope">
                    <el-option-group
                        key="Select the scope"
                        label="Select the scope">
                        <el-option label="Title" value="title"></el-option>
                        <el-option label="Content" value="content"></el-option>
                    </el-option-group>
                    </el-select>
                </el-form-item>

                <el-form-item prop="rule" >
                    <el-select 
                    v-model="searchForm.rule" 
                    placeholder="Select rule">
                        <el-option-group
                            key="Order by"
                            label="Order by">
                            <el-option label="Heat" value="heat"></el-option>
                            <el-option label="Released time" value="releasedTime"></el-option>
                        </el-option-group>
                    </el-select>
                </el-form-item>

                <el-form-item size="medium">
                    <el-button
                    type="primary"
                    native-type="submit"
                    size="medium"
                    style="width:100%"
                    v-on:click="search">
                        Search
                    </el-button>
                </el-form-item>
            </el-form>
            <br>
            <br>
        </el-col>
        <el-col v-if="afterSearch" :span="6" :offset="1">
            <span class="mySpan">Search results</span>
        </el-col>
        <el-col v-if="afterSearch" :span="18" :offset="3">
            <el-divider></el-divider>
            <div v-if="isEmpty">No results.</div>
            <div v-if="notEmpty" class=" layui-row" >
                <div class=" layui-col-md3 card layui-col-md-offset3" 
                v-for="(image,i) in images.slice((currentPage-1)*6,currentPage*6)" :key="i">
                    <router-link
                    :to="'../../../imageDetail/'+image.imageId">
                    <img :src="baseURL + image.path" class=" newImg" />
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
                    </div>
                    </router-link>
                </div>
            </div>
            <div v-if="notEmpty" class="block">
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
    <footerbar v-if="afterSearch"></footerbar>
</div>
</template>

<script>
import navbar from "../components/Navbar"
import footerbar from "../components/Footer"

export default {
    components: {navbar, footerbar},
    inject: ['reload'],
    name: 'Search',
    data(){
        return{
            searchForm: {
                input: '',
                scope: 'title',
                rule: 'heat'
            },
            images: [],
            input: ' ',
            baseURL: '/static/travel-images/medium/',
            afterSearch: false,
            isEmpty: false,
            notEmpty: true,
            loading: false,
            total: 0,
            currentPage: 1,
        }
    },
    created(){
        this.searchForm.input = this.$route.params.input;
        if(this.searchForm.input==" "){
            this.searchForm.input = ""
        }
        this.searchForm.scope = this.$route.params.scope;
        this.searchForm.rule = this.$route.params.rule;

        this.loading = true;
        this.$axios
          .post("/search", {
             input: this.searchForm.input,
             scope: this.searchForm.scope,
             rule: this.searchForm.rule
          })
          .then(resp => {
             if(resp.status === 200){
                if(resp.data === "empty"){
                    this.isEmpty = true;
                    this.notEmpty = false;
                }
                else{
                    this.images = resp.data;
                    this.isEmpty = false;
                    this.notEmpty = true;
                    this.total = this.images.length;
                }
                this.loading = false;
                this.afterSearch = true
             }
             else {
                this.errorNotification();
                this.loading = false;
             }
           })
           .catch(error => {
              console.log(error);
              this.errorNotification();
              this.loading = false;
           });

    },
    methods: {
      search(){
        if(!this.searchForm.input){
           this.searchForm.input = " ";
        }
        this.$router.replace("/search/"+this.searchForm.input+"/"+this.searchForm.scope
            +"/"+this.searchForm.rule);
        this.reload();
      },
      handleCurrentChange(val){
          this.currentPage = val;
      },
      errorNotification(){
        this.$message.error('Requset error, please try again');
      },
    }
}
</script>

<style scoped>
.mySpan{
    color: #009688;
    font-size: larger;
    margin-left: 20px;
}
.newImg{
    height: 210px;
    width: 100%;
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
.emptyFooter{
    margin-top: 30%;
}
</style>