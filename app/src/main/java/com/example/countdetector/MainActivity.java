package com.example.countdetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private TextView tv_small, tv_large;
    private Button button;
    private ImageView iv_image;
    int imageCount, smallCount, largeCount, buttonCount = 0;
    private ConnectivityReceiver mConnectivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_small = (TextView) findViewById(R.id.tv_smallText);
        tv_large = (TextView) findViewById(R.id.tv_largeText);
        button = (Button) findViewById(R.id.button);
        iv_image = (ImageView) findViewById(R.id.iv_image);

        mConnectivityReceiver = new ConnectivityReceiver(MainActivity.this);

        tv_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallCount += 1;
                new Utility().makeServerCall(MainActivity.this, "small", smallCount, new StatusInterface() {
                    @Override
                    public void onSuccess() {
                        smallCount = 0;
                    }

                    @Override
                    public void onFailure(int code) {
                        switch (code) {
                            case 1000:
                                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                break;
                            case 2000:
                                new Utility().setInPersistenceStorage(MainActivity.this, "small_text", smallCount);
                                break;
                            default:
                                //do nothing
                        }
                    }
                });
            }
        });

        tv_large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                largeCount += 1;
                new Utility().makeServerCall(MainActivity.this, "large", largeCount, new StatusInterface() {
                    @Override
                    public void onSuccess() {
                        largeCount = 0;
                    }

                    @Override
                    public void onFailure(int code) {
                        switch (code) {
                            case 1000:
                                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                break;
                            case 2000:
                                new Utility().setInPersistenceStorage(MainActivity.this, "large_text", largeCount);
                                break;
                            default:
                                //do nothing
                        }
                    }
                });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    buttonCount += 1;
                    new Utility().makeServerCall(MainActivity.this, "button", buttonCount, new StatusInterface() {
                        @Override
                        public void onSuccess() {
                            buttonCount = 0;
                        }

                        @Override
                        public void onFailure(int code) {
                            switch (code) {
                                case 1000:
                                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                    break;
                                case 2000:
                                    new Utility().setInPersistenceStorage(MainActivity.this, "button", buttonCount);
                                    break;
                                default:
                                    //do nothing
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imageCount += 1;

                    new Utility().makeServerCall(MainActivity.this, "image", imageCount, new StatusInterface() {
                        @Override
                        public void onSuccess() {
                            imageCount = 0;
                        }

                        @Override
                        public void onFailure(int code) {
                            switch (code) {
                                case 1000:
                                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                    break;
                                case 2000:
                                    new Utility().setInPersistenceStorage(MainActivity.this, "image", imageCount);
                                    break;
                                default:
                                    //do nothing
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mConnectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mConnectivityReceiver);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            Log.d("Message", "onNetworkConnectionChanged: internet is connected");
            final int sCount = new Utility().getSharePreference(MainActivity.this, "small_text");
            final int lCount = new Utility().getSharePreference(MainActivity.this, "large_text");
            final int bCount = new Utility().getSharePreference(MainActivity.this, "button");
            final int iCount = new Utility().getSharePreference(MainActivity.this, "image");
            if (sCount > 0) {
                try {
                    new Utility().makeServerCall(MainActivity.this, "small", sCount, new StatusInterface() {
                        @Override
                        public void onSuccess() {
                            new Utility().setInPersistenceStorage(MainActivity.this, "small_text", 0);

                        }

                        @Override
                        public void onFailure(int code) {
                            switch (code) {
                                case 1000:
                                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                    break;
                                case 2000:
                                    new Utility().setInPersistenceStorage(MainActivity.this, "small_text", smallCount + sCount);
                                    break;
                                default:
                                    //do nothing
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (lCount > 0) {
                try {
                    new Utility().makeServerCall(MainActivity.this, "large", lCount, new StatusInterface() {
                        @Override
                        public void onSuccess() {
                            new Utility().setInPersistenceStorage(MainActivity.this, "large_text", 0);

                        }

                        @Override
                        public void onFailure(int code) {
                            switch (code) {
                                case 1000:
                                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                    break;
                                case 2000:
                                    new Utility().setInPersistenceStorage(MainActivity.this, "large_text", largeCount + lCount);
                                    break;
                                default:
                                    //do nothing
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (iCount > 0) {
                try {
                    new Utility().makeServerCall(MainActivity.this, "image", iCount, new StatusInterface() {
                        @Override
                        public void onSuccess() {
                            new Utility().setInPersistenceStorage(MainActivity.this, "image", 0);

                        }

                        @Override
                        public void onFailure(int code) {
                            switch (code) {
                                case 1000:
                                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                    break;
                                case 2000:
                                    new Utility().setInPersistenceStorage(MainActivity.this, "image", imageCount + iCount);
                                    break;
                                default:
                                    //do nothing
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (bCount > 0) {
                try {
                    new Utility().makeServerCall(MainActivity.this, "button", bCount, new StatusInterface() {
                        @Override
                        public void onSuccess() {
                            new Utility().setInPersistenceStorage(MainActivity.this, "button", 0);

                        }

                        @Override
                        public void onFailure(int code) {
                            switch (code) {
                                case 1000:
                                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                    break;
                                case 2000:
                                    new Utility().setInPersistenceStorage(MainActivity.this, "button", buttonCount + bCount);
                                    break;
                                default:
                                    //do nothing
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("Message", "onNetworkConnectionChanged: internet is not connected");
        }
    }
}
