<template>
    <v-container>
        <v-row justify="center" align="center">
            <v-col cols="12">
                <v-card elevation="1" width="100%">
                    <v-card-title>
                        <v-text-field
                            outlined
                            rounded
                            label="Name"
                            @keyup.native="textChanged"
                            :rules="nameRules"
                            v-model="computedSelectedNote.name"
                        ></v-text-field>
                    </v-card-title>
                    <v-card-text class="black--text">
                        <vue-editor
                            @text-change="textChanged"
                            v-model="computedSelectedNote.body"
                            :editorToolbar="customToolbar"
                        ></vue-editor>
                    </v-card-text>
                    <v-card-actions>
                        <v-label>Last saved {{getRelativeUpdateTime(computedSelectedNote.lastUpdatedTimestamp)}}</v-label>
                        <v-spacer />
                        <v-btn @click="openShareOverlay" small rounded text>
                            <v-icon color="primary">mdi-share-variant</v-icon>
                        </v-btn>
                        <v-btn @click="deleteOverlay = !deleteOverlay" small rounded text>
                            <v-icon color="primary">mdi-delete</v-icon>
                        </v-btn>

                        <v-overlay v-model="shareOverlay" absolute>
                            <v-card light>
                                <v-card-title class="justify-center">Share Note</v-card-title>

                                <v-card-text>
                                    <v-autocomplete
                                        v-model="selectedUser"
                                        :items="users"
                                        :loading="isLoading"
                                        :search-input.sync="userSearchInput"
                                        item-text="username"
                                        item-value="uuid"
                                        placeholder="Search for user"
                                        prepend-icon="mdi-user-search"
                                        return-object
                                        clearable
                                    />
                                    <p
                                        v-if="selectedUser"
                                        class="text-center black--text"
                                    >User '{{selectedUser.username}}' will have edit access to '{{computedSelectedNote.name}}'</p>
                                </v-card-text>

                                <v-card-actions>
                                    <v-spacer />
                                    <v-btn
                                        class="font-weight-bold"
                                        color="primary"
                                        @click.stop="shareNote"
                                        small
                                        rounded
                                    >Share</v-btn>
                                    <v-spacer />
                                    <v-btn
                                        class="font-weight-bold"
                                        color="primary"
                                        @click="shareOverlay = !shareOverlay"
                                        small
                                        rounded
                                    >Cancel</v-btn>
                                    <v-spacer />
                                </v-card-actions>
                            </v-card>
                        </v-overlay>

                        <v-overlay v-model="deleteOverlay" absolute>
                            <v-card light>
                                <v-card-title
                                    class="justify-center"
                                >Delete Note</v-card-title>
                                <v-card-text class="text-center black--text">Confirm delete of note '{{computedSelectedNote.name}}'</v-card-text>
                                <v-card-actions>
                                    <v-spacer />
                                    <v-btn
                                        class="font-weight-bold"
                                        color="primary"
                                        @click.stop="deleteNote"
                                        small
                                        rounded
                                    >Delete</v-btn>
                                    <v-spacer />
                                    <v-btn
                                        class="font-weight-bold"
                                        color="primary"
                                        @click="deleteOverlay = !deleteOverlay"
                                        small
                                        rounded
                                    >Cancel</v-btn>
                                    <v-spacer />
                                </v-card-actions>
                            </v-card>
                        </v-overlay>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import { VueEditor } from "vue2-editor";
import moment from "moment";
import { mapState } from "vuex";
import { SERVER_URL } from "../config.js";
import axios from "axios";

export default {
    components: { VueEditor },

    data() {
        return {
            nameRules: [v => !!v || "Name is required"],
            customToolbar: [
                ["bold", "italic", "underline"],
                [{ list: "ordered" }, { list: "bullet" }],
                ["code-block"]
            ],
            deleteOverlay: false,
            shareOverlay: false,

            descriptionLimit: 60,
            users: [],
            isLoading: false,
            selectedUser: null,
            userSearchInput: null
        };
    },

    computed: {
        ...mapState({
            notes: state => state.notes,
            selectedNoteUuid: state => state.selectedNoteUuid,
            ignoreNextEditEvent: state => state.ignoreNextEditEvent
        }),
        computedSelectedNote() {
            var selectedNote = this.notes.filter(
                note => note.uuid == this.selectedNoteUuid
            )[0];
            if (selectedNote == null) {
                return {
                    name: "Loading..."
                };
            }
            return selectedNote;
        }
    },

    watch: {
        selectedNoteUuid() {
            this.deleteOverlay = false;
            this.shareOverlay = false;
        },
        shareOverlay(shareOverlayValue) {
            if (!shareOverlayValue) {
                this.users = [];
                this.selectedUser = null;
                this.userSearchInput = null;
            }
        },
        userSearchInput(searchValue) {
            //only call the api ever 3rd character? this isn't great obviously
            if (
                this.isLoading ||
                searchValue == null ||
                searchValue.length % 3 != 1
            ) {
                return;
            }

            this.isLoading = true;

            axios
                .get(SERVER_URL + "/users?username=" + searchValue)
                .then(response => {
                    this.users = response.data;
                    this.isLoading = false;
                })
                .catch(error => {
                    console.error(error);
                    this.isLoading = false;
                });
        }
    },

    methods: {
        getRelativeUpdateTime(time) {
            return moment(time).fromNow();
        },
        textChanged() {
            if (this.ignoreNextEditEvent) {
                this.$store.commit("setIgnoreNextEditEvent", false);
            } else {
                this.$store.commit("addUnsavedNote", {
                    uuid: this.selectedNoteUuid,
                    time: Date.now()
                });
            }
        },
        deleteNote() {
            this.$store.commit("deleteNote", this.selectedNoteUuid);
        },
        shareNote() {
            console.info(this.model);
        },
        openShareOverlay() {
            this.shareOverlay = true;
        }
    }
};
</script>