<template>
    <v-app id="inspire">
        <v-content>
            <v-container class="fill-height" fluid>
                <v-row align="center" justify="center">
                    <v-col cols="12" sm="8" md="4">
                        <v-card class="elevation-12">
                            <v-toolbar color="primary" dark flat>
                                <v-toolbar-title>Create Account</v-toolbar-title>
                                <v-spacer></v-spacer>
                            </v-toolbar>
                            <v-card-text>
                                <v-form>
                                    <v-text-field
                                        v-model="username"
                                        label="Username"
                                        name="username"
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
                                {{info}}
                            </v-card-text>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn @click.stop="createUser" color="primary">Create account</v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-col>
                </v-row>
            </v-container>
            <v-snackbar
                color="success"
                v-model="createAccountResponseSuccessSnackbar"
            >{{ createAccountResponseMessage }}</v-snackbar>
            <v-snackbar
                color="error"
                v-model="createAccountResponseErrorSnackbar"
            >{{ createAccountResponseMessage }}</v-snackbar>
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
            info: "",
            createAccountResponseSuccessSnackbar: false,
            createAccountResponseErrorSnackbar: false,
            createAccountResponseMessage: ""
        };
    },

    props: {
        source: String
    },

    methods: {
        createUser() {
            var createUserRequest = {
                password: this.password,
                user: {
                    username: this.username
                }
            };

            axios
                .put(SERVER_URL + "/users", createUserRequest)
                .then(response => {
                    response;
                    this.createAccountResponseSuccessSnackbar = true;
                    this.createAccountResponseMessage =
                        "Account created successfully!";
                })
                .catch(error => {
                    this.createAccountResponseErrorSnackbar = true;

                    if (error.response.status === 400) {
                        this.createAccountResponseMessage =
                            "Username/password invalid";
                    } else {
                        this.createAccountResponseMessage;
                    }
                });
        }
    }
};
</script>
