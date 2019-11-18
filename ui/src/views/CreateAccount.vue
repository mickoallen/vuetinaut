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
                            <v-form ref="createAccountForm" v-model="valid" lazy-validation>
                                <v-text-field
                                    v-model="username"
                                    label="Username"
                                    name="login"
                                    prepend-icon="mdi-account"
                                    type="text"
                                    :rules="nameRules"
                                ></v-text-field>
                                <v-text-field
                                    v-model="passwordOne"
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
                            <v-spacer />
                            <v-btn
                                :disabled="!valid"
                                @click.stop="createUser"
                                class="font-weight-bold"
                                color="primary"
                            >Create Account</v-btn>
                            <v-spacer />
                        </v-card-actions>
                        <v-card-text>
                            <p class="text-center">
                                You can try out MNotes as a
                                <router-link to="/guest-login">guest</router-link>
                            </p>
                            <p class="text-center">
                                <router-link to="/login">Back to login</router-link>
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
            passwordOne: "",
            valid: null,
            nameRules: [
                v => !!v || "Required"
            ]
        };
    },

    methods: {
        createUser() {
            if (!this.$refs.createAccountForm.validate()) {
                return;
            }

            var createUserRequest = {
                password: this.passwordOne,
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
