package com.Appzia.enclosure.Adapter.viewholders;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import de.hdodenhof.circleimageview.CircleImageView;
import eightbitlab.com.blurview.BlurView;

public class ReceiverViewHolder extends RecyclerView.ViewHolder {
    public TextView recMessage, recTime, docName, extension, cName, cPhone, firstText, grpMsgName, readMore;
    public AppCompatImageView recImg;
    public BlurView blurView;
    public LinearLayout docLyt, contactContainer, viewContact;
    public ConstraintLayout menu2;
    public RelativeLayout receivervideoLyt;
    public LinearLayout miceContainer, replydatalyt, llnew, cnamenamelyt, progresslyt, delete;
    public CircleImageView miceUImage;
    public AppCompatImageButton micePlay;
    public LinearProgressIndicator miceProgressbar;
    public TextView miceTiming, repliedData, forwarded;
    public View replyDevider, viewbarlyt1;
    public LinearProgressIndicator viewnew;
    public ImageView recVideo, videoicon;
    public RelativeLayout receiverImgLyt;
    public FloatingActionButton downlaod;
    public ProgressBar progressBarImageview;
    public View blur;
    public FloatingActionButton downlaodVideo;
    public ProgressBar progressBarVideo;
    public View blurVideo;
    public TextView captionText;
    public RelativeLayout richLinkViewLyt;

    public ImageView linkImg, linkImg2;
    public TextView linkTitle;
    public TextView linkDesc;
    public TextView link, linkActualUrl;
    public LinearLayout datelyt, MainReceiverBox;
    public TextView dateTxt;
    public TextView emojiText;
    public CardView emojiTextCard, cardview;

    public FrameLayout videoFrame;

    public TextView docSizeExtension;
    public TextView docSize, originalNumber, originalName;
    public ImageView pdfPreview;
    public LinearLayout docFileIcon;
    public CardView pdfcard;
    public LinearLayoutCompat originalDelete, originalAdd;
    public ImageView personaddTheme;

    public RelativeLayout docDownloadControlsReceiver;
    public FloatingActionButton downlaodDocReceiver;
    public ProgressBar progressBarDocReceiver;
    public TextView downloadPercentageDocReceiver;
    public ImageView pauseButtonDocReceiver;

    public ReceiverViewHolder(@NonNull View itemView) {
        super(itemView);
        recMessage = itemView.findViewById(R.id.recMessage);
        recTime = itemView.findViewById(R.id.recTime);
        docName = itemView.findViewById(R.id.docName);
        recImg = itemView.findViewById(R.id.recImg);
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
        receivervideoLyt = itemView.findViewById(R.id.receivervideoLyt);
        recVideo = itemView.findViewById(R.id.recVideo);
        receiverImgLyt = itemView.findViewById(R.id.receiverImgLyt);

        downlaod = itemView.findViewById(R.id.downlaod);
        progressBarImageview = itemView.findViewById(R.id.progressBar);
        blur = itemView.findViewById(R.id.blur);

        downlaodVideo = itemView.findViewById(R.id.downlaodVideo);
        progressBarVideo = itemView.findViewById(R.id.progressBarVideo);
        blurVideo = itemView.findViewById(R.id.blurVideo);
        captionText = itemView.findViewById(R.id.captionText);
        readMore = itemView.findViewById(R.id.readMore);

        richLinkViewLyt = itemView.findViewById(R.id.richLinkViewLyt);
        linkImg = itemView.findViewById(R.id.linkImg);
        linkImg2 = itemView.findViewById(R.id.linkImg2);
        linkTitle = itemView.findViewById(R.id.linkTitle);
        linkDesc = itemView.findViewById(R.id.linkDesc);
        link = itemView.findViewById(R.id.link);
        linkActualUrl = itemView.findViewById(R.id.linkActualUrl);
        datelyt = itemView.findViewById(R.id.datelyt);
        dateTxt = itemView.findViewById(R.id.dateTxt);
        MainReceiverBox = itemView.findViewById(R.id.MainReceiverBox);
        emojiText = itemView.findViewById(R.id.emojiText);
        emojiTextCard = itemView.findViewById(R.id.emojiTextCard);
        videoFrame = itemView.findViewById(R.id.videoFrame);
        pdfPreview = itemView.findViewById(R.id.pdfPreview);
        pdfcard = itemView.findViewById(R.id.pdfcard);
        docFileIcon = itemView.findViewById(R.id.docFileIcon);
        docSizeExtension = itemView.findViewById(R.id.docSizeExtension);
        docSize = itemView.findViewById(R.id.docSize);
        videoicon = itemView.findViewById(R.id.videoicon);
        cardview = itemView.findViewById(R.id.cardview);
        originalNumber = itemView.findViewById(R.id.originalNumber);
        originalName = itemView.findViewById(R.id.originalName);
        originalDelete = itemView.findViewById(R.id.originalDelete);
        originalAdd = itemView.findViewById(R.id.originalAdd);
        personaddTheme = itemView.findViewById(R.id.personaddTheme);

        docDownloadControlsReceiver = itemView.findViewById(R.id.docDownloadControlsReceiver);
        downlaodDocReceiver = itemView.findViewById(R.id.downlaodDocReceiver);
        progressBarDocReceiver = itemView.findViewById(R.id.progressBarDocReceiver);
        downloadPercentageDocReceiver = itemView.findViewById(R.id.downloadPercentageDocReceiver);
        pauseButtonDocReceiver = itemView.findViewById(R.id.pauseButtonDocReceiver);
    }
}
