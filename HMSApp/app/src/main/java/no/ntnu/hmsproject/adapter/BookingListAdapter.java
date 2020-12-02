package no.ntnu.hmsproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.Booking;
import no.ntnu.hmsproject.ui.hmsservice.booking.FragmentBookingListDirections;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.BookingViewHolder> {
    List<Booking> bookings;
    OnClickListener listener = position -> {};

    public BookingListAdapter() {
        bookings = new ArrayList<>();
    }


    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        holder.bookingId.setText(bookings.get(position).getBookingId());
        holder.startDate.setText(bookings.get(position).getBookingStartDate());
        holder.endDate.setText(bookings.get(position).getBookingEndDate());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    interface OnClickListener {
        void onClick(int position);
    }

    public void setBookingsList(ArrayList<Booking> list) {
        this.bookings = list;
        notifyDataSetChanged();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView bookingId;
        TextView startDate;
        TextView endDate;

        public BookingViewHolder(@NonNull View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView bookingIdView = view.findViewById(R.id.booking_id);
                    int bookingid = Integer.parseInt(bookingIdView.getText().toString());
                    FragmentBookingListDirections.ActionNavBookingListToBookingDetails action = FragmentBookingListDirections.actionNavBookingListToBookingDetails(bookingid);
                    Navigation.findNavController(view).navigate(action);
                }
            });
            this.bookingId = view.findViewById(R.id.booking_id);
            this.startDate = view.findViewById(R.id.booking_startdate);
            this.endDate = view.findViewById(R.id.booking_enddate);
        }
    }
}
