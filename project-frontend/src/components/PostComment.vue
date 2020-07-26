<template>
<el-container>
    <el-main>
        <el-col :span="9" :offset="0">
            <br>
            <br>
            <span class="mySpan">Comments</span> &nbsp;&nbsp;&nbsp;
        </el-col>
        <el-col :span="18" :offset="3">
            <el-divider></el-divider>
            <el-col :span="16" :offset="1">
                <el-form 
                    @submit.native.prevent
                    status-icon
                    :model="commentForm"
                    ref="commentForm"
                    label-position="left"
                    label-width="0px"
                    v-loading="loading"
                >
                    <el-form-item prop="input">
                        <el-input
                            type="textarea"
                            :autosize="{ minRows: 4, maxRows: 6}"
                            maxlength="60"
                            show-word-limit
                            v-if="isLogin"
                            placeholder="Write down your comments here..."
                            v-model="commentForm.input">
                        </el-input>
                        <el-input
                            type="textarea"
                            :autosize="{ minRows: 4, maxRows: 6}"
                            maxlength="60"
                            show-word-limit
                            v-else
                            disabled
                            placeholder="Log in to write down your comments here..."
                            v-model="commentForm.input">
                        </el-input>
                    </el-form-item>

                    <el-form-item size="medium">
                        <el-button
                        type="primary"
                        native-type="submit"
                        :disabled="isDisabled"
                        size="medium"
                        style="width:15%"
                        class="btn"
                        v-on:click="post">
                            Post
                        </el-button>
                        <el-button 
                        style="width:15%"
                        plain
                        @click="resetForm('commentForm')"
                        v-if="isLogin">
                            Reset
                        </el-button>
                        <el-button 
                        style="width:20%"
                        plain
                        @click="resetForm('commentForm')"
                        v-else
                        disabled>
                            Reset
                        </el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-col>
    </el-main>
</el-container>
</template>

<script>
export default {
    name: 'postComment',
    inject: ['reload'],
    data() {
        return{
            commentForm: {
                input:'',
            },
            loading: false,
            isLogin: false,
        };
    },
    created(){
        if(this.$store.state.token){
            this.isLogin = true;
        }
    },
    computed:{
      isDisabled(){
        return !(this.commentForm.input);
      },
    },
    methods: {
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        post(){
            this.loading = true;
            this.$axios
                .post("/postComment", {
                    imageId: this.$route.params.imageId,
                    content: this.commentForm.input
                })
                .then(resp => {
                    if(resp.status === 200){
                        this.loading = false;
                        this.reload();
                        this.$message({
                            type: "success",
                            message: 'Comment successfully',
                        });
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
            this.$message.error('Requset error, please try again');
        },
    }
}
</script>

<style scoped>
.mySpan{
  color: #009688;
  font-size: larger;
}
.btn{
    margin-left: -500px;
}
</style>