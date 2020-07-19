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

              <el-form-item prop="file">
                  <el-upload
                    ref="upload"
                    action
                    drag
                    :auto-upload="false"
                    :limit="1"
                    :http-request="upload"
                    accept="image/*"
                    :on-exceed="handleExceed"
                    :on-change="handleChange"
                    :file-list="files"
                    list-type="picture"
                    style="width: 100%"
                    >
                    <em class="el-icon-upload"></em>
                    <div class="el-upload__text">
                        Drag image here or 
                        <em>click me</em>
                    </div>
                    <div class="el-upload__tip" slot="tip">Accept jpg, jpeg, png. Only one image is required.</div>
                  </el-upload>
              </el-form-item>

              <el-form-item size="medium">
                <el-button
                type="primary"
                native-type="submit"
                :disabled="isDisabled"
                size="medium"
                style="width:100%"
                v-on:click="submitForm('uploadForm')">
                    Submit
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
    inject: ["reload"],
    data(){
        return{
            imageUrl: '',
            loading: false,
            countries: [],
            cities: [],
            cityLoading: false,
            file:null,
            files:[],
            isModify: false,
            fileChange: false,
            timeout:  null,
            uploadForm: {
                title: '',
                author: '',
                content: '',
                description: '',
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
        this.uploadForm.author = this.$store.state.cur_user;
        this.$axios
        .get("/getCountries",{})
        .then(resp => {
            if(resp.status === 200){
              this.countries = resp.data;
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
      submitForm(formName){
        this.$refs[formName].validate(valid => {
            // First contribution but no image
            if (valid) {
            if(!this.isModify && !this.file){
                this.$notify({
                    type: "warning",
                    dangerouslyUseHTMLString: true,
                    title: 'Upload fail',
                    message:
                    "<strong style='color:teal'>Please upload your image!</strong>"
                });
                return;
            }
            this.loading = true;
            this.$refs["upload"].submit();

            var config = {
                headers: { "Content-Type": "multipart/form-data" }
            };

            var url = this.isModify?'/modifyImg':'/submitImg';
            console.log(this.file);
            var data = new FormData();
            data.append("title", this.uploadForm.title);
            data.append("author", this.uploadForm.author);
            data.append("content", this.uploadForm.content);
            data.append("description", this.uploadForm.description);
            data.append("country", this.uploadForm.country);
            data.append("city", this.uploadForm.city);
            data.append("file", this.file);

            this.$axios
                .post(url, data, config)
                .then(resp => {
                    if(resp.status === 200){
                        this.reload();
                        this.$notify({
                            type: "success",
                            center: true,
                            dangerouslyUseHTMLString: true,
                            message:
                            "<strong style='color:teal'>Upload successfully!</strong>"
                        });
                        this.reload();
                    }
                })
                .catch(error => {
                    console.log(error);
                });
            }else{
                this.errorNotification();
                this.loading = false;
            }
        
        });
      },
      
      handleExceed() {
        this.$notify({
          dangerouslyUseHTMLString: true,
            type:'warning',
            title: 'Upload fail',
            message: '<strong style="color:teal">Only 1 file is required!</strong>',
        });
      },
      handleChange(file){
          this.file = file;
          this.fileChange = true;
      },
      upload(params) {
        this.file = params.file;
        return;
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
.myBtn{
    width: 400%;
    margin-left: -50px;
}
</style>