package com.example.android.mymoiveapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public static final String MY_MOVIE_OBJECT ="myMovie";
    public  final String BASEPOSTERURL = "http://image.tmdb.org/t/p";

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.detail, container, false);

        TextView movieTitle = (TextView) view.findViewById(R.id.movie_title);
        ImageView moviePostView = (ImageView) view.findViewById(R.id.movie_poster);
        TextView movieRelease = (TextView) view.findViewById(R.id.moive_release);
        TextView movieVote = (TextView) view.findViewById(R.id.movie_vote);

        TextView movieDescription  = (TextView) view.findViewById(R.id.movie_desc);



        //The detail activity
        Intent intent = getActivity().getIntent();

        if(intent!=null && intent.hasExtra(MY_MOVIE_OBJECT))
        {
            Parcelable movie = intent.getParcelableExtra(MY_MOVIE_OBJECT);

            if(movie instanceof MyMovie)
            {

                MyMovie myMovie =(MyMovie)movie;

               //set title
                movieTitle.setText(myMovie.getOriginalTitle());

                //set poster
                String posterUrl = myMovie.getPosterUrl();
                String fullPosterUrl = BASEPOSTERURL+"/w500"+posterUrl;
                Picasso.with(getActivity()).load(fullPosterUrl).into(moviePostView);


                //set release date
                movieRelease.setText(myMovie.getRelease());

                //set movie vote
                movieVote.setText(myMovie.getVoteAverage()+"/10");

                //set overview
                movieDescription.setText(myMovie.getOverview());


            }

        }


        return view;

    }
}
