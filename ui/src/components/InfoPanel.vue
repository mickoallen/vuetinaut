<template>
    <v-list dense>
        <v-list-item>
            <v-btn @click.stop="logout">Logout</v-btn>
        </v-list-item>
    </v-list>
</template>

<script>
import axios from "axios";
import { SERVER_URL } from "../config.js";

export default {
    components: {  },
    props: {
    },

    data() {
        return {
        };
    },

    methods: {
        logout() {
            axios
                .post(SERVER_URL + "/logout")
                .then(response => {
                    response;
                    this.$router.replace("/login");
                })
                .catch(error => {
                    this.handleApiError(error);
                });
        },

        handleApiError(error) {
            console.log(error);
            this.$router.replace("/login");
        }
    }
};
</script>