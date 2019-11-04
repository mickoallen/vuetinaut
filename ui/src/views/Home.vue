<template>
    <v-app>
        <v-content>
            <v-card>
                <v-card-text>{{notepads}}</v-card-text>
            </v-card>
        </v-content>
        <v-snackbar :color="getSnackColor" v-model="showSnack">{{ snackMessage }}</v-snackbar>
    </v-app>
</template>

<script>
import axios from "axios";
import { SERVER_URL } from "../config.js";

export default {
    data() {
        return {
            notepads: [],
            showSnack: false,
            snackError: false,
            snackMessage: ""
        };
    },
    computed: {
        getSnackColor() {
            return this.snackError ? "error" : "success";
        }
    },
    components: {},

    beforeMount() {
        axios
            .get(SERVER_URL + "/notepads")
            .then(response => {
                this.notepads = response;
            })
            .catch(error => {
                this.showSnack = true;
                this.snackError = true;
                this.snackMessage = "Error logging in";

                if (error.response.status === 400) {
                    this.snackMessage = "Username/password invalid";
                } else {
                    this.snackMessage = error;
                }
            });
    },

    methods() {}
};
</script>
