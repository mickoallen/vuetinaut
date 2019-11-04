# Vuetinaut

Template project for prototyping cool things.


##### Built from....
Micronaut, Vue + Vuetify, Postgres

## Features


- Swagger doc generation from API
- JS client generated from Swagger docs
    
docker run --rm -v ${PWD}:/local openapitools/openapi-generator-cli generate -i local/target/classes/META-INF/swagger/vuetinaut-0.1.yml -g javascript -o /local/ui/client

docker run --rm -v ${PWD}:/local openapitools/openapi-generator-cli generate \
    -i https://raw.githubusercontent.com/openapitools/openapi-generator/master/modules/openapi-generator/src/test/resources/2_0/petstore.yaml \
    -g go \
    -o /local/out/go