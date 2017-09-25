#include <jni.h>
#include <string>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/features2d.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/opencv.hpp>
#include <vector>

using namespace std;
using namespace cv;

extern "C"
JNIEXPORT void JNICALL
Java_io_a2xe_experiments_myapplicationc_OpenCVCamera_findFeatures(
        JNIEnv*,
        jobject /* this */,
        jlong addrGray, jlong addrRgba) {

    Mat& mGr  = *(Mat*)addrGray;
    Mat& mRgb = *(Mat*)addrRgba;
    vector<KeyPoint> v;

    Ptr<FeatureDetector> detector = FastFeatureDetector::create(50);
    detector->detect(mGr, v);
    for( unsigned int i = 0; i < v.size(); i++ )
    {
        const KeyPoint& kp = v[i];
        circle(mRgb, Point(kp.pt.x, kp.pt.y), 10, Scalar(255,0,0,255));
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_io_a2xe_experiments_myapplicationc_OpenCVCamera_findBinaryFeatures(
        JNIEnv*,
        jobject /* this */,
        jlong addrGray, jlong addrRgba) {


    Mat& mGr  = *(Mat*)addrGray;
    Mat& mRgb = *(Mat*)addrRgba;
    vector<KeyPoint> v;

    Ptr<FeatureDetector> detector = ORB::create();
    detector->detect(mGr, v);
    for( unsigned int i = 0; i < v.size(); i++ )
    {
        const KeyPoint& kp = v[i];
        circle(mRgb, Point((int) kp.pt.x, (int) kp.pt.y), 10, Scalar(255, 255, 0, 255));
    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_io_a2xe_experiments_myapplicationc_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

IplImage* skipNFrames(CvCapture* capture, int n)
{
    for(int i = 0; i < n; ++i)
    {
        if(cvQueryFrame(capture) == NULL)
        {
            return NULL;
        }
    }

    return cvQueryFrame(capture);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_a2xe_experiments_myapplicationc_ReproduceVideoActivity_openVideo(
        JNIEnv* env,
        jobject /* this */,
        jstring path) {

    const char* nPath = env->GetStringUTFChars(path, NULL);

    CvCapture *capture = cvCaptureFromFile(nPath);
    if (capture == NULL)
        printf("camera is null\n");
    else
        printf("camera is not null");

    IplImage* frame = NULL;

    do {

        frame = skipNFrames(capture, 4);

        double t1 = (double)cvGetTickCount();
        IplImage *img = cvQueryFrame(capture);

        double t2 = (double)cvGetTickCount();

        printf("time: %gms  fps: %.2g\n",(t2-t1)/(cvGetTickFrequency()*1000.), 1000./((t2-t1)/(cvGetTickFrequency()*1000.)));
    } while(frame != NULL );

    cvReleaseCapture(&capture);
}

