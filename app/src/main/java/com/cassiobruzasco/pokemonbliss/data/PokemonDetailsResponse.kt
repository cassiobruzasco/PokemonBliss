package com.cassiobruzasco.pokemonbliss.data

import com.google.gson.annotations.SerializedName

data class PokemonDetailsResponse(
    @SerializedName("base_experience") val exp: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("moves") val moves: MutableList<MovesModel>,
    @SerializedName("name") val name: String,
    @SerializedName("types") val types: MutableList<TypesModel>,
    @SerializedName("weight") val weight: Int,

)

data class MovesModel(
    @SerializedName("move") val move: MoveModel,
    @SerializedName("version_group_details") val version: MutableList<VersionModel>
)

data class MoveModel(
    @SerializedName("name") val name: String
)

data class VersionModel(
    @SerializedName("level_learned_at") val levelLearnedAt: Int
)

data class TypesModel(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: TypeModel
)

data class TypeModel(
    @SerializedName("name") val name: String
)