package com.WisdomMonkey.CinemaTicketBooking_Backend.enums;

public enum SwipeDirection {
    TOP,    // seen + like + save
    BOTTOM, // seen + dislike + don't save
    LEFT,   // not seen + dislike
    RIGHT   // not seen + like + save
}