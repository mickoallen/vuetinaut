<template>
    <v-app>
        <v-content>
            <v-card>                
                <NotepadList />
            </v-card>
        </v-content>
        <v-snackbar :color="getSnackColor" v-model="showSnack">{{ snackMessage }}</v-snackbar>
    </v-app>
</template>

<script>
import NotepadList from "../components/NotepadList"

export default {
    components: {NotepadList},

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

    beforeMount() {
    },

    methods() {}
};
</script>
