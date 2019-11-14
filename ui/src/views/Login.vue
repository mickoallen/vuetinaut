<template>
    <v-app id="inspire">
        <v-content>
            <v-container class="fill-height" fluid>
                <v-row align="center" justify="center">
                    <v-col cols="12" sm="8" md="4">
                        <v-card class="elevation-12">
                            <v-toolbar color="primary" dark flat>
                                <v-toolbar-title>Login</v-toolbar-title>
                                <v-spacer></v-spacer>
                            </v-toolbar>
                            <v-card-text>
                                <v-form>
                                    <v-text-field
                                        v-model="username"
                                        label="Login"
                                        name="login"
                                        prepend-icon="mdi-account"
                                        type="text"
                                    ></v-text-field>
                                    <v-text-field
                                        v-model="password"
                                        id="password"
                                        label="Password"
                                        name="password"
                                        prepend-icon="mdi-lock"
                                        type="password"
                                    ></v-text-field>
                                </v-form>
                            </v-card-text>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn @click.stop="login" color="primary">Login</v-btn>
                            </v-card-actions>
                            <v-card-text>
                                <router-link to="/create-account">
                                    <p class="text-center">Don't have an account?</p>
                                </router-link>
                                <router-link to="/forgot-password">
                                    <p class="text-center">Forgot password?</p>
                                </router-link>
                            </v-card-text>
                        </v-card>
                    </v-col>
                </v-row>
            </v-container>
            <v-snackbar :color="getSnackColor" v-model="showSnack">{{ snackMessage }}</v-snackbar>
        </v-content>
    </v-app>
</template>

<script>
import axios from "axios";
import { SERVER_URL } from "../config.js";

export default {
    data() {
        return {
            username: "",
            password: "",
            snackError: false,
            showSnack: false,
            snackMessage: ""
        };
    },

    computed: {
        getSnackColor() {
            return this.snackError ? "error" : "success";
        }
    },

    props: {
        source: String
    },

    methods: {
        login() {
            var loginRequest = {
                username: this.username,
                password: this.password
            };

            axios
                .post(SERVER_URL + "/login", loginRequest)
                .then(response => {
                    response;
                    this.showSnack = true;
                    this.snackError = false;
                    this.snackMessage = "Logged in";
                    this.$router.replace("/");
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
        }
    }
};
</script>