<template>
    <navbar></navbar>
</template>

<script>
import navbar from "../components/Navbar"
import footerbar from "../components/footer"

export default {
    components: {navbar, footerbar},
    name: "ImageDetail",
    data(){
        return{

        };
    },
    methods:{
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
        .post("ImageDetail",{
            imageId: this.$route.params.imageId
        })
        .then(resp => {
            if(resp.status === 200){

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