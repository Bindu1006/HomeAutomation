package com.example.shruti.homeautomation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Shruti on 5/1/16.
 */
public class ScreenSlidePageFragment extends Fragment {

    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putString("hi","hiiiii");
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        // Set the title view to show the page number.
        if(mPageNumber == 0){
            System.out.println("hi" + mPageNumber);
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.title_template_step1, mPageNumber + 1));

            ImageView step1_image = (ImageView) rootView.findViewById(R.id.id_steps_image);
            step1_image.setImageResource(R.drawable.home);

            ((TextView) rootView.findViewById(R.id.id_steps)).setText(
                    getString(R.string.step1_details));

        }  if(mPageNumber == 1){
            System.out.println("hi"+mPageNumber);
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.title_template_step2, mPageNumber + 1));

            ImageView step1_image = (ImageView) rootView.findViewById(R.id.id_steps_image);
            step1_image.setImageResource(R.drawable.home);

            ((TextView) rootView.findViewById(R.id.id_steps)).setText(
                    getString(R.string.step2_details));

        } if(mPageNumber == 2){
            System.out.println("hi"+mPageNumber);
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.title_template_step2, mPageNumber + 1));

            ImageView step1_image = (ImageView) rootView.findViewById(R.id.id_steps_image);
            step1_image.setImageResource(R.drawable.home);

            ((TextView) rootView.findViewById(R.id.id_steps)).setText(
                    getString(R.string.step2_details));

        } if(mPageNumber == 3){
            System.out.println("hi"+mPageNumber);
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.title_template_step2, mPageNumber + 1));

            ImageView step1_image = (ImageView) rootView.findViewById(R.id.id_steps_image);
            step1_image.setImageResource(R.drawable.home);

            ((TextView) rootView.findViewById(R.id.id_steps)).setText(
                    getString(R.string.step2_details));

        } if(mPageNumber == 4){
            System.out.println("hi"+mPageNumber);
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.title_template_step2, mPageNumber + 1));

            ImageView step1_image = (ImageView) rootView.findViewById(R.id.id_steps_image);
            step1_image.setImageResource(R.drawable.home);

            ((TextView) rootView.findViewById(R.id.id_steps)).setText(
                    getString(R.string.step2_details));

        }


        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

}
