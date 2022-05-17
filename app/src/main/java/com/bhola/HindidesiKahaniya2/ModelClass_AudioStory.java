package com.bhola.HindidesiKahaniya2;

public class ModelClass_AudioStory {
    String storyName;
    String storyURL;

    public ModelClass_AudioStory() {
    }

    public ModelClass_AudioStory(String storyName, String storyURL) {
        this.storyName = storyName;
        this.storyURL = storyURL;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public String getStoryURL() {
        return storyURL;
    }

    public void setStoryURL(String storyURL) {
        this.storyURL = storyURL;
    }
}
