package co.carlosandresjimenez.android.bakingappkotlin.data.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by carlosjimenez on 1/1/18.
 */

data class BakingRecipe(
        val id: Int,
        val name: String,
        val ingredients: List<BakingIngredient>,
        val steps: List<BakingStep>,
        val servings: String,
        val image: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            id = parcel.readInt(),
            name = parcel.readString(),
            ingredients = parcel.createTypedArrayList(BakingIngredient),
            steps = parcel.createTypedArrayList(BakingStep),
            servings = parcel.readString(),
            image = parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeTypedList(ingredients)
        parcel.writeTypedList(steps)
        parcel.writeString(servings)
        parcel.writeString(image)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<BakingRecipe> {

        override fun createFromParcel(parcel: Parcel): BakingRecipe = BakingRecipe(parcel)

        override fun newArray(size: Int): Array<BakingRecipe?> = arrayOfNulls(size)

    }
}

data class BakingIngredient(
        val quantity: Float,
        val measure: String,
        val ingredient: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            quantity = parcel.readFloat(),
            measure = parcel.readString(),
            ingredient = parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(quantity)
        parcel.writeString(measure)
        parcel.writeString(ingredient)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<BakingIngredient> {

        override fun createFromParcel(parcel: Parcel): BakingIngredient = BakingIngredient(parcel)

        override fun newArray(size: Int): Array<BakingIngredient?> = arrayOfNulls(size)

    }
}

data class BakingStep(
        val id: Int,
        val shortDescription: String,
        val description: String,
        val videoURL: String,
        val thumbnailURL: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            id = parcel.readInt(),
            shortDescription = parcel.readString(),
            description = parcel.readString(),
            videoURL = parcel.readString(),
            thumbnailURL = parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(shortDescription)
        parcel.writeString(description)
        parcel.writeString(videoURL)
        parcel.writeString(thumbnailURL)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<BakingStep> {

        override fun createFromParcel(parcel: Parcel): BakingStep = BakingStep(parcel)

        override fun newArray(size: Int): Array<BakingStep?> = arrayOfNulls(size)

    }
}