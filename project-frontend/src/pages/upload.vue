<template>
<div>
    <navbar></navbar>
    <el-container>
      <el-main>
          <el-col :span="6" :offset="1">
            <span class="mySpan">Upload</span>
        </el-col>
        <el-col :span="18" :offset="3">
            <el-divider></el-divider>
        </el-col>
        <el-col :span="6" :offset="9">
            <el-form 
            @submit.native.prevent
            status-icon
            :model="uploadForm"
            :rules="rules"
            ref="uploadForm"
            label-position="left"
            label-width="0px"
            v-loading="loading"
            >
              <el-form-item prop="title" >
                <el-input
                    type="text"
                    v-model="uploadForm.title"
                    auto-complete="off"
                    placeholder="Title"
                ></el-input>
              </el-form-item>

              <el-form-item prop="author" >
                <el-input
                    type="text"
                    v-model="uploadForm.author"
                    auto-complete="off"
                    placeholder="Author"
                ></el-input>
              </el-form-item>

              <el-form-item prop="content" >
                <el-input
                    type="text"
                    v-model="uploadForm.content"
                    auto-complete="off"
                    placeholder="Content"
                ></el-input>
              </el-form-item>

              <el-form-item prop="description" >
                  <el-input
                    type="textarea"
                    :autosize="{ minRows: 2, maxRows: 4}"
                    auto-complete="off"
                    placeholder="Description"
                    v-model="uploadForm.description">
                </el-input>
              </el-form-item>

              <el-form-item prop="country">
                <el-select v-model="uploadForm.country" filterable placeholder="Country" style="width:100%">
                    <el-option
                    v-for="(country,i) in countries"
                    :key="i"
                    :label="country.value"
                    :value="country.value"
                    :@change="calCities"
                    >
                    </el-option>
                </el-select>
              </el-form-item>

              <el-form-item prop="city" v-loading="cityLoading">
                <el-select v-model="uploadForm.city" :disabled="isCityDisabled" filterable placeholder="City" style="width:100%">
                    <el-option
                    v-for="(city,j) in cities"
                    :key="j"
                    :label="city.value"
                    :disabled="isCityDisabled"
                    :value="city.value">
                    </el-option>
                </el-select>
              </el-form-item>

              <el-form-item size="medium">
                <el-button
                type="primary"
                native-type="submit"
                :disabled="isDisabled"
                size="medium"
                style="width:100%"
                v-on:click="upload">
                    Upload
                </el-button>
              </el-form-item>

              <el-form-item size="medium">
                <el-button 
                style="width:100%"
                plain
                @click="resetForm('uploadForm')">
                    Reset
                </el-button>
              </el-form-item>
            </el-form>
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
    name: 'Upload',
    data(){
        return{
            imageUrl: '',
            loading: false,
            countries: [],
            cities: [],
            cityLoading: false,
            uploadForm: {
                title: '',
                author: '',
                content: '',
                description: '',
                timeout:  null,
                country: '',
                city: ''
            },
            rules: {
                title: [
                    {required:true,message:"Title is required",blur:"change"},
                ],
                author: [
                    {required:true,message:"Author is required",blur:"change"},
                ],
                content: [
                    {required:true,message:"Content is required",blur:"change"},
                ],
                description: [
                    {required:true,message:"Description is required",blur:"change"},
                ],
                country: [
                    {required:true,message:"Country is required",blur:"change"},
                ],
                city: [
                    {required:true,message:"City is required",blur:"change"},
                ],
            }
        }
    },
    computed: {
        isDisabled(){
            return !(this.uploadForm.title && this.uploadForm.author
                && this.uploadForm.content && this.uploadForm.description
                && this.uploadForm.country && this.uploadForm.city)
        },
        isCityDisabled(){
            return !(this.uploadForm.country);
        },
        calCities(){
            if(this.uploadForm.country){
                this.cityLoading = true;
                this.$axios
                    .post("/getCities",{
                        country: this.uploadForm.country
                    })
                    .then(resp => {
                    if(resp.status === 200){
                        this.cities = resp.data;
                        this.cityLoading = false;
                    } else {
                        this.errorNotification();
                        this.cityLoading = false;
                    }
                })
                .catch(error => {
                    console.log(error);
                    this.errorNotification();
                    this.cityLoading = false;
                });
            }
      },
    },
    created(){
        // get login info
        if(!this.$store.state.token){
            this.$router.replace("/login");
            this.$notify({
                type:'error',
                dangerouslyUseHTMLString: true,
                title: 'Request error',
                message: '<strong style="color:teal">Please login to upload your photos</strong>'
            });
        };
        this.$axios
        .get("/getCountries",{})
        .then(resp => {
            if(resp.status === 200){
              this.countries = resp.data;
              console.log(resp.data);
            } else {
              this.errorNotification();
            }
        })
        .catch(error => {
            console.log(error);
            this.errorNotification();
        });
    },
    methods: {
      upload(){

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
    mounted() {
    },
}
</script>

<style scoped>
.mySpan{
  color: #009688;
  font-size: larger;
  margin-left: 20px;
}
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>