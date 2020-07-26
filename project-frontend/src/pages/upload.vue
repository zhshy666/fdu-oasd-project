<template>
<div>
    <navbar></navbar>
    <el-container>
      <el-main>
          <el-col :span="6" :offset="1">
            <span v-if="isModify" class="mySpan">Modify</span>
            <span v-else class="mySpan">Upload</span>
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
              <el-form-item prop="title" label="Title" class="is-required">
                <el-input
                    type="text"
                    v-model="uploadForm.title"
                    auto-complete="off"
                    placeholder="Title of your image"
                ></el-input>
              </el-form-item>

              <el-form-item prop="author" label="Author" class="is-required">
                <el-input
                    type="text"
                    v-model="uploadForm.author"
                    auto-complete="off"
                    placeholder="Author of your image"
                ></el-input>
              </el-form-item>

              <el-form-item prop="content" label="Content" class="is-required">
                <el-input
                    type="text"
                    v-model="uploadForm.content"
                    auto-complete="off"
                    placeholder="Content of your image"
                ></el-input>
              </el-form-item>

              <el-form-item prop="description" label="Description" class="is-required">
                  <el-input
                    type="textarea"
                    :autosize="{ minRows: 2, maxRows: 4}"
                    auto-complete="off"
                    placeholder="Description of your image"
                    v-model="uploadForm.description">
                </el-input>
              </el-form-item>

              <el-form-item prop="country" label="Country" class="is-required">
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

              <el-form-item prop="city" v-loading="cityLoading" label="City" class="is-required">
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

              <el-form-item prop="file" label="File" class="is-required">
                  <el-upload
                    v-if="isModify"
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

                  <el-upload
                    v-else
                    ref="upload"
                    action
                    drag
                    :auto-upload="false"
                    :limit="1"
                    :http-request="upload"
                    accept="image/*"
                    :on-exceed="handleExceed"
                    :on-change="handleChange"
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
                <el-button v-if="isModify"
                type="primary"
                native-type="submit"
                :disabled="isDisabled"
                size="medium"
                style="width:100%"
                v-on:click="submitForm('uploadForm')">
                    Modify
                </el-button>
                <el-button v-else
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
    <footerbar></footerbar>
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
            loading: false,
            countries: [],
            cities: [],
            cityLoading: false,
            file:null,
            files:[{name: '', url: ''}],
            isModify: false,
            fileChange: false,
            timeout:  null,
            heat: '',
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
            this.$message.error('Please login to upload images');
        };
        if(this.$route.params.imageId){
            console.log(this.$route.params.imageId);
            this.$axios
                .post("/imageDetail", {
                    imageId: this.$route.params.imageId
                })
                .then(resp => {
                    if(resp.status === 200){
                        this.isModify = true;
                        this.heat = resp.data.image.heat;
                        this.uploadForm.title = resp.data.image.title;
                        this.uploadForm.author = resp.data.image.author;
                        this.uploadForm.content = resp.data.image.content;
                        this.uploadForm.description = resp.data.image.description;
                        this.uploadForm.country = resp.data.country;
                        this.uploadForm.city = resp.data.city;
                        this.files[0].name = this.uploadForm.title;
                        this.files[0].url = "/static/travel-images/medium/" + resp.data.image.path;
                    }
                })
        }
        else{
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
        }
    },
    methods: {
      submitForm(formName){
        this.$refs[formName].validate(valid => {
            // First contribution but no image
            if (valid) {
                if(!this.isModify && !this.file){
                    this.$message.error('Image is required');
                    return;
                }
                this.$confirm("Are you sure to submit?", "Upload confirm", {
                    confirmButtonText: "Yes",
                    cancelButtonText: "No"
                })
                .then(() => {
                    this.loading = true;
                    this.$refs["upload"].submit();

                    var config = {
                        headers: { "Content-Type": "multipart/form-data" }
                    };

                    var url = this.isModify?'/modifyImg':'/submitImg';
                    
                    var data = new FormData();
                    data.append("title", this.uploadForm.title);
                    data.append("author", this.uploadForm.author);
                    data.append("content", this.uploadForm.content);
                    data.append("description", this.uploadForm.description);
                    data.append("country", this.uploadForm.country);
                    data.append("city", this.uploadForm.city);
                    data.append("file", this.file);

                    if(this.isModify){
                        data.append("imageId", this.$route.params.imageId);
                        if(!this.file){
                            data.append("modifyImg", "false");
                        }else if(this.file){
                            data.append("modifyImg", "true");
                        }
                    }
                    this.$axios
                        .post(url, data, config)
                        .then(resp => {
                            if(resp.status === 200){
                                this.reload();
                                if(this.isModify){
                                    this.$message({
                                        type: "success",
                                        message: 'Modify successfully',
                                    });
                                    this.$router.replace("/home");
                                }else{
                                    this.$message({
                                        type: "success",
                                        message: 'Upload successfully',
                                    });
                                }
                                this.reload();
                            }
                        })
                        .catch(error => {
                            console.log(error);
                        });
                }) 
                .catch(error => {});
            }else{
                this.errorNotification();
                this.loading = false;
            }
            
        });
      },
      
      handleExceed() {
        this.$message.error('Only 1 file is required');
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
        this.$message.error('Requset error, please try again');
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