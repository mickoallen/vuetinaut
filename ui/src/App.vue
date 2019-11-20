<template>
    <v-app>
        <v-content>
            <v-container fluid>
                <router-view></router-view>
                <v-snackbar color="primary" v-model="successSnackbar">
                    <v-row align="center" justify="center">{{ successMessage.message }}</v-row>
                </v-snackbar>
                <v-snackbar color="error" v-model="errorSnackbar">
                    <v-row align="center" justify="center">{{ errorMessage.message }}</v-row>
                </v-snackbar>
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
import { mapState } from "vuex";

export default {
    data() {
        return {
            successSnackbar: false,
            errorSnackbar: false
        };
    },

    computed: {
        ...mapState({
            currentUser: state => state.currentUser,
            successMessage: state => state.successMessage,
            errorMessage: state => state.errorMessage
        })
    },

    watch: {
        errorMessage() {
            this.errorSnackbar = true;
            setTimeout(function() {
                3000, (this.errorSnackbar = false);
            });
        },
        successMessage() {
            this.successSnackbar = true;
            setTimeout(function() {
                3000, (this.successSnackbar = false);
            });
        }
    }
};
</script>

<style>
#app {
    font-family: "Avenir", Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    background-image: url("./assets/bright-geometric-shape-background/872.jpg");
    background-repeat: no-repeat;
    background-attachment: fixed;
}
</style>
