# ui

## Project setup
```
npm install
```

### Build ui client (will improve later)
```docker run --rm -v ${PWD}:/local openapitools/openapi-generator-cli generate -i local/target/classes/META-INF/swagger/vuetinaut-0.1.yml -g javascript -o /local/ui/client```
```cd client```
```npm install```
```change version to 0.1.0```
```npm run build```
```browserify dist/index.js bundle.js```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Run your tests
```
npm run test
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
