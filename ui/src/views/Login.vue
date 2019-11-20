<template>
    <div>
        <v-container>
            <v-col>
                <v-row justify="center">
                    <v-card elevation="5">
                        <v-card-title>
                            <v-spacer />
                            <v-img src="../assets/logo-main.png"></v-img>
                            <v-spacer />
                        </v-card-title>
                        <v-card-text>
                            <v-form ref="loginForm" v-model="valid" lazy-validation>
                                <v-text-field
                                    v-model="username"
                                    label="Login"
                                    name="login"
                                    prepend-icon="mdi-account"
                                    type="text"
                                    :rules="nameRules"
                                ></v-text-field>
                                <v-text-field
                                    v-model="password"
                                    id="password"
                                    label="Password"
                                    name="password"
                                    prepend-icon="mdi-lock"
                                    type="password"
                                    :rules="nameRules"
                                ></v-text-field>
                            </v-form>
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn
                                @click.stop="login"
                                class="font-weight-bold"
                                color="primary"
                            >Login</v-btn>
                            <v-spacer></v-spacer>
                        </v-card-actions>
                        <v-card-text>
                            <p class="text-center">
                                Don't have an account?
                                <router-link to="/create-account">Create one</router-link> or
                                <router-link to="/guest-login">Try as guest</router-link>
                            </p>
                        </v-card-text>
                    </v-card>
                </v-row>
            </v-col>
        </v-container>
    </div>
</template>

<script>
import axios from "axios";
import { SERVER_URL } from "../config.js";

export default {
    data() {
        return {
            username: "",
            password: "",
            valid: true,
            nameRules: [v => !!v || "Required"]
        };
    },

    computed: {
    },

    props: {
        source: String
    },

    methods: {
        login() {
            if (!this.$refs.loginForm.validate()) {
                return;
            }

            var loginRequest = {
                username: this.username,
                password: this.password
            };

            axios
                .post(SERVER_URL + "/login", loginRequest)
                .then(response => {
                    response;
                    this.$store.commit("successSnackbar", "Logged in");
                    this.$router.replace("/notes");
                })
                .catch(error => {
                    error;
                    this.$store.commit("errorSnackbar", "Invalid username/password");
                });
        }
    }
};
</script>