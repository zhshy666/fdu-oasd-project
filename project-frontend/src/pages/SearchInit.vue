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
      </el-main>
    </el-container>
</div>
</template>

<script>
import navbar from "../components/Navbar"

export default {
    components: {navbar},
    name: 'SearchInit',
    data(){
        return{
            searchForm: {
                input: '',
                scope: 'title',
                rule: 'heat'
            },
        }
    },
    methods: {
      search(){
        if(!this.searchForm.input){
           this.searchForm.input = " ";
        }
        this.$router.replace("/search/"+this.searchForm.input+"/"+this.searchForm.scope
            +"/"+this.searchForm.rule);
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