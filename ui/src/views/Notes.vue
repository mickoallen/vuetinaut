<template>
    <div>
        <NotepadList />
        <v-snackbar :color="getSnackColor" v-model="showSnack">{{ snackMessage }}</v-snackbar>
    </div>
</template>

<script>
import NotepadList from "../components/NotepadList";

export default {
    components: { NotepadList },

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

    beforeMount() {}

};
</script>
