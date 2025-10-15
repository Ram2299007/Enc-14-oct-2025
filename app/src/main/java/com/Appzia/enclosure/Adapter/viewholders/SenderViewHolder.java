package com.Appzia.enclosure.Adapter.viewholders;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.WaveformView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import de.hdodenhof.circleimageview.CircleImageView;

public class SenderViewHolder extends RecyclerView.ViewHolder {
    public TextView sendMessage, sendTime, docName, extension, cName, cPhone, firstText, forwarded, grpMsgName, readMore;
    public AppCompatImageView senderImg;
    public LinearLayout MainSenderBox, richBox;
    public LinearLayout docLyt, contactContainer, viewContact, llnew;
    public LinearLayout miceContainer, replydatalyt, cnamenamelyt, progresslyt, delete;
    public CircleImageView miceUImage;
    public AppCompatImageButton micePlay;
    public LinearProgressIndicator miceProgressbar;
    public TextView miceTiming, repliedData;
    public View replyDevider, viewbarlyt1;
    public LinearProgressIndicator viewnew;
    public ConstraintLayout menu2;
    public ImageView senderVideo;
    public RelativeLayout senderImgLyt;
    public FloatingActionButton downlaod;
    public ProgressBar progressBarImageview;
    public View blur;
    public RelativeLayout sendervideoLyt;
    public FloatingActionButton downlaodVideo;
    public ProgressBar progressBarVideo;
    public View blurVideo;
    public TextView captionText;
    public RelativeLayout richLinkViewLyt;
    public ImageView linkImg, linkImg2, videoicon;
    public TextView linkTitle;
    public TextView linkDesc;
    public TextView link, linkActualUrl;
    public LinearLayout datelyt;
    public TextView dateTxt;
    public TextView emojiText;
    public TextView docSizeExtension;
    public TextView docSize;
    public CardView emojiTextCard;
    public WaveformView waveformView;
    public FrameLayout frame;
    public CircularProgressIndicator circularProgressBar;
    public ImageView imageInsideProgressBar;
    public ImageView pdfPreview;
    public LinearLayout docFileIcon;
    public CardView pdfcard;
    public FrameLayout videoFrame;
    
    // senderImgBunchLyt variables
    public com.google.android.material.imageview.ShapeableImageView img1, img2, img3, img4;
    public TextView overlayTextImg;
    public com.google.android.material.floatingactionbutton.FloatingActionButton downlaodImgBunch;
    public TextView downloadPercentageImageSenderBunch;
    public ImageButton pauseButtonImageSenderBunch;
    public ProgressBar progressBarImgBunch;

    public SenderViewHolder(@NonNull View itemView) {
        super(itemView);
        sendMessage = itemView.findViewById(R.id.sendMessage);
        sendTime = itemView.findViewById(R.id.sendTime);
        docName = itemView.findViewById(R.id.docName);
        senderImg = itemView.findViewById(R.id.senderImg);
        docLyt = itemView.findViewById(R.id.docLyt);
        extension = itemView.findViewById(R.id.extension);
        contactContainer = itemView.findViewById(R.id.contactContainer);
        cName = itemView.findViewById(R.id.cName);
        cPhone = itemView.findViewById(R.id.cPhone);
        viewContact = itemView.findViewById(R.id.viewContact);
        firstText = itemView.findViewById(R.id.firstText);
        miceContainer = itemView.findViewById(R.id.miceContainer);
        miceUImage = itemView.findViewById(R.id.miceUImage);
        micePlay = itemView.findViewById(R.id.micePlay);
        miceProgressbar = itemView.findViewById(R.id.miceProgressbar);
        miceTiming = itemView.findViewById(R.id.miceTiming);
        replyDevider = itemView.findViewById(R.id.replyDevider);
        repliedData = itemView.findViewById(R.id.repliedData);
        replydatalyt = itemView.findViewById(R.id.replydatalyt);
        viewbarlyt1 = itemView.findViewById(R.id.viewbarlyt1);
        llnew = itemView.findViewById(R.id.llnew);
        viewnew = itemView.findViewById(R.id.viewnew);
        cnamenamelyt = itemView.findViewById(R.id.cnamenamelyt);
        progresslyt = itemView.findViewById(R.id.progresslyt);
        menu2 = itemView.findViewById(R.id.menu2);
        delete = itemView.findViewById(R.id.delete);
        forwarded = itemView.findViewById(R.id.forwarded);
        grpMsgName = itemView.findViewById(R.id.grpMsgName);
        senderVideo = itemView.findViewById(R.id.senderVideo);
        sendervideoLyt = itemView.findViewById(R.id.sendervideoLyt);
        senderImgLyt = itemView.findViewById(R.id.senderImgLyt);
        downlaod = itemView.findViewById(R.id.downlaod);
        progressBarImageview = itemView.findViewById(R.id.progressBar);
        blur = itemView.findViewById(R.id.blur);
        downlaodVideo = itemView.findViewById(R.id.downlaodVideo);
        progressBarVideo = itemView.findViewById(R.id.progressBarVideo);
        blurVideo = itemView.findViewById(R.id.blurVideo);
        captionText = itemView.findViewById(R.id.captionText);
        richLinkViewLyt = itemView.findViewById(R.id.richLinkViewLyt);
        linkImg = itemView.findViewById(R.id.linkImg);
        linkImg2 = itemView.findViewById(R.id.linkImg2);
        linkTitle = itemView.findViewById(R.id.linkTitle);
        linkDesc = itemView.findViewById(R.id.linkDesc);
        link = itemView.findViewById(R.id.link);
        linkActualUrl = itemView.findViewById(R.id.linkActualUrl);
        readMore = itemView.findViewById(R.id.readMore);
        datelyt = itemView.findViewById(R.id.datelyt);
        dateTxt = itemView.findViewById(R.id.dateTxt);
        MainSenderBox = itemView.findViewById(R.id.MainSenderBox);
        richBox = itemView.findViewById(R.id.richBox);
        emojiText = itemView.findViewById(R.id.emojiText);
        emojiTextCard = itemView.findViewById(R.id.emojiTextCard);
        videoFrame = itemView.findViewById(R.id.videoFrame);
        pdfPreview = itemView.findViewById(R.id.pdfPreview);
        pdfcard = itemView.findViewById(R.id.pdfcard);
        docFileIcon = itemView.findViewById(R.id.docFileIcon);
        docSizeExtension = itemView.findViewById(R.id.docSizeExtension);
        docSize = itemView.findViewById(R.id.docSize);
        videoicon = itemView.findViewById(R.id.videoicon);
        
        // senderImgBunchLyt findViewById calls
        img1 = itemView.findViewById(R.id.img1);
        img2 = itemView.findViewById(R.id.img2);
        img3 = itemView.findViewById(R.id.img3);
        img4 = itemView.findViewById(R.id.img4);
        overlayTextImg = itemView.findViewById(R.id.overlayTextImg);
        downlaodImgBunch = itemView.findViewById(R.id.downlaodImgBunch);
        downloadPercentageImageSenderBunch = itemView.findViewById(R.id.downloadPercentageImageSenderBunch);
        pauseButtonImageSenderBunch = itemView.findViewById(R.id.pauseButtonImageSenderBunch);
        progressBarImgBunch = itemView.findViewById(R.id.progressBarImgBunch);
    }
}


