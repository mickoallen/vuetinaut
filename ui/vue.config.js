module.exports = {
  "transpileDependencies": [
    "vuetify"
  ],
  devServer: {
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Controller-Allow-Methods": "GET, POST, PUT, DELETE, PATCH, OPTIONS",
      "Access-Control-Allow-Headers": "X-Requested-With, content-type, Authorization, Location, set-cookie"
    }
  }
}