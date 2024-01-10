package com.example.eastcyclingclub;

import android.os.Looper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ParticipantEventSelectAndRateUnitTest {

    private static final String EMPTY_SEARCH_QUERY = "";

    private static final String VALID_EVENT_RATING = "3";
    private static final String INVALID_EVENT_RATING = "6";
    private static final String VALID_EVENT_COMMENT = "i liked the race.";
    private static final String INVALID_EVENT_COMMENT = "";

    // this is here for now until rating has been implemented
//
//
//    private static final int MINIMUM_EVENT_RATING = 1;
//    private static final int MAXIMUM_EVENT_RATING = 5;
//    public boolean validateEventRating(int rating){
//        return !(rating < MINIMUM_EVENT_RATING) && !(rating > MAXIMUM_EVENT_RATING);
//    }

    @Test
    public void testallValidCredentials(){
        ParticipantActivityAddRating testedEvent = new ParticipantActivityAddRating();
        boolean result = testedEvent.reviewIsValid(VALID_EVENT_COMMENT, VALID_EVENT_RATING);
        assertTrue(result);
    }


    @Test
    public void testAllInvalidCredentials(){
        ParticipantActivityAddRating testedEvent = new ParticipantActivityAddRating();
        boolean result = testedEvent.reviewIsValid(INVALID_EVENT_COMMENT, INVALID_EVENT_RATING);
        assertFalse(result);
    }


    @Test
    public void testInvalidRating(){
        ParticipantActivityAddRating testedEvent = new ParticipantActivityAddRating();
                boolean result = testedEvent.reviewIsValid(VALID_EVENT_COMMENT, INVALID_EVENT_RATING);
        assertFalse(result);
    }


    @Test
    public void testInvalidComment(){
        ParticipantActivityAddRating testedEvent = new ParticipantActivityAddRating();
        boolean result = testedEvent.reviewIsValid(INVALID_EVENT_COMMENT, VALID_EVENT_RATING);
        assertFalse(result);
    }
}
