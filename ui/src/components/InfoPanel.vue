<template>
    <v-list dense>
        <v-list-item>
            <p>Username: {{this.currentUser.username}}</p>
        </v-list-item>
        <v-list-item>
            <v-btn small rounded @click.stop="logout" class="font-weight-bold" color="primary">Logout</v-btn>
        </v-list-item>
    </v-list>
</template>

<script>
import axios from "axios";
import { SERVER_URL } from "../config.js";
import { mapState } from "vuex";

export default {
    data() {
        return {
        };
    },

    computed: {
        ...mapState({
            currentUser: state => state.currentUser,
            notes: state => state.notes
        })
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