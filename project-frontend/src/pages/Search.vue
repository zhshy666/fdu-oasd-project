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
                            <el-option label="Released time" value="time"></el-option>
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
        <el-col>
        </el-col>
      </el-main>
    </el-container>
</div>
</template>

<script>
import navbar from "../components/Navbar"

export default {
    components: {navbar},
    name: 'Search',
    data(){
        return{
            searchForm: {
                input: '',
                scope: 'title',
                rule: 'heat'
            },
            loading: false
        }
    },
    methods: {
      search(){
        this.loading = true;
        this.$axios
          .post("/search", {
             input: this.searchForm.input,
             scope: this.searchForm.scope,
             rule: this.searchForm.rule
          })
          .then(resp => {
             if(resp.status === 200){
                 console.log(resp.data);
                 this.loading = false;
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

</style>