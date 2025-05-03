package com.onmoim.core.constant

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.onmoim.R

enum class InterestCategory(
    @DrawableRes val iconId: Int,
    @StringRes val labelId: Int
) {
    EXERCISE(
        iconId = R.drawable.ic_exercise,
        labelId = R.string.interest_exercise
    ),
    SOCIAL(
        iconId = R.drawable.ic_social,
        labelId = R.string.interest_social
    ),
    BOOK(
        iconId = R.drawable.ic_book,
        labelId = R.string.interest_book
    ),
    TRAVEL(
        iconId = R.drawable.ic_travel,
        labelId = R.string.interest_travel
    ),
    MUSIC(
        iconId = R.drawable.ic_music,
        labelId = R.string.interest_music
    ),
    JOB(
        iconId = R.drawable.ic_job,
        labelId = R.string.interest_job
    ),
    CULTURE(
        iconId = R.drawable.ic_culture,
        labelId = R.string.interest_culture
    ),
    LANGUAGE(
        iconId = R.drawable.ic_language,
        labelId = R.string.interest_language
    ),
    GAME(
        iconId = R.drawable.ic_game,
        labelId = R.string.interest_game
    ),
    CRAFT(
        iconId = R.drawable.ic_craft,
        labelId = R.string.interest_craft
    ),
    DANCE(
        iconId = R.drawable.ic_dance,
        labelId = R.string.interest_dance
    ),
    VOLUNTEER_ACTIVITY(
        iconId = R.drawable.ic_volunteer_activity,
        labelId = R.string.interest_volunteer_activity
    ),
    PHOTO(
        iconId = R.drawable.ic_photo,
        labelId = R.string.interest_photo
    ),
    SELF_IMPROVEMENT(
        iconId = R.drawable.ic_self_improvement,
        labelId = R.string.interest_self_improvement
    ),
    SPORTS(
        iconId = R.drawable.ic_sports,
        labelId = R.string.interest_sports
    ),
    PET(
        iconId = R.drawable.ic_pet,
        labelId = R.string.interest_pet
    ),
    COOK(
        iconId = R.drawable.ic_cook,
        labelId = R.string.interest_cook
    ),
    CAR(
        iconId = R.drawable.ic_car,
        labelId = R.string.interest_car
    )
}