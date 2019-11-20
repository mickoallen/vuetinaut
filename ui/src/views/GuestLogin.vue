<template>
    <div>
        <v-container>
            <v-col>
                <v-row align="center" justify="center">
                    <div class="text-center">
                        <v-progress-circular :size="50" color="primary" indeterminate></v-progress-circular>
                    </div>
                </v-row>
            </v-col>
        </v-container>
    </div>
</template>

<script>
import axios from "axios";
import { SERVER_URL } from "../config.js";

export default {
    mounted() {
        this.createGuestUser();
    },
    methods: {
        createGuestUser() {
            var password = this.makeId(20);
            var username = "guest-" + this.makeId(7);
            var createUserRequest = {
                password: password,
                user: {
                    username: username,
                    userType: "GUEST"
                }
            };

            axios
                .put(SERVER_URL + "/users", createUserRequest)
                .then(response => {
                    response;
                    this.createAccountResponseSuccessSnackbar = true;
                    this.createAccountResponseMessage =
                        "Account created successfully!";

                    var loginRequest = {
                        username: username,
                        password: password
                    };

                    axios
                        .post(SERVER_URL + "/login", loginRequest)
                        .then(response => {
                            response;
                            this.showSnack = true;
                            this.snackError = false;
                            this.snackMessage = "Logged in";
                            this.$router.replace("/");
                            this.$store.commit("successSnackbar", "Logged in as guest");
                        })
                        .catch(error => {
                            error;
                            this.$router.replace("/login");
                            this.$store.commit("errorSnackbar", "Failed to login as guest");
                        });
                })
                .catch(error => {
                    error;
                    this.$router.replace("/");
                    this.$store.commit("errorSnackbar", "Failed to login as guest");
                });
        },
        makeId(length) {
            var result = "";
            var characters =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            var charactersLength = characters.length;
            for (var i = 0; i < length; i++) {
                result += characters.charAt(
                    Math.floor(Math.random() * charactersLength)
                );
            }
            return result;
        }
    }
};
</script>
