Please add these line on Review activity inside
protected voide onBindViewHolder (....)
{
	holder.review_username.settext(model.getUsername());
	.....
	.....
	.....
	//add these lines start from here
	holder.itemView.setOnClickListener(view ->
                {

                    String UsernameIntent = model.getUsername();
                    String ReviewIntent = model.getReview();
                    String RatingIntent = model.getRating();
                    String DateIntent = model.getDate();
                    String ClassIntent = model.getClassTaken();
                    Intent user_selected_intent = new Intent(getApplicationContext(), ReplyingActivity.class);
                    user_selected_intent.putExtra("username_from_list", UsernameIntent);
                    user_selected_intent.putExtra("review_from_list", ReviewIntent);
                    user_selected_intent.putExtra("rating_from_list", RatingIntent);
                    user_selected_intent.putExtra("date_from_list", DateIntent);
                    user_selected_intent.putExtra("class_from_list", ClassIntent);

                    startActivity(user_selected_intent);

                    startActivity(new Intent());
                    Toast.makeText(ReviewActivity.this, "Now viewing comment from "+model.getUsername(), Toast.LENGTH_SHORT).show();

                });
	//to here
}



