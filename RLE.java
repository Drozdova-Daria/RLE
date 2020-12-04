package com.company;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class RLE {
    private final String input, output;
    private final int length, bufferSize;
    private int rleSize;
    private byte[] bytes;
    private byte[] array;
    private String action;
    RLE(ConfigRider config) {
        input = config.GetInputFile();
        output = config.GetOutputFile();
        length = config.GetLengthSequence();
        bufferSize = config.GetBufferSize();
        action = config.GetAction();
    }
    void RunLengthEncoding() {
        try {
            FileInputStream fileInput = new FileInputStream(input);
            if(fileInput.available() == 0) {
                Errors.ERROR_EMPTY_SEQUENCE.PrintErrorInLog();
            } else {
                while (fileInput.available() > 0 ) {
                    int count = 0;
                    int currentBufferSize;
                    if(bufferSize < fileInput.available()) {
                        currentBufferSize = bufferSize;
                    } else {
                        currentBufferSize = fileInput.available();
                    }
                    bytes = new byte[currentBufferSize];
                    fileInput.read(bytes, (count++) * bufferSize, currentBufferSize);
                    /*for(int i = 0; i < currentBufferSize; i++) {
                        bytes[i] = (byte)fileInput.read();
                    }*/
                    if (action.equals("compression"))
                        array = RLEArr(bytes, length);
                    else if (action.equals("recovery"))
                        array = GetOriginalSequence(bytes, currentBufferSize);
                    try {
                        FileOutputStream fileOutput = new FileOutputStream(output);
                        fileOutput.write(array);
                    } catch (IOException exception) {
                        Errors.ERROR_OUTPUT_FILE_OPENING.PrintErrorInLog();
                    }
                }

            }
        } catch (IOException exception) {
            Errors.ERROR_INPUT_FILE_OPENING.PrintErrorInLog();
        }
    }

    private byte[] RLEArr (byte[] array, int length) {
        byte[] arrRLE = new byte[bufferSize * 2];
        byte[] previous;
        byte[] current;
        int indexRLE = 0;
        previous = initArray(array, length, 0);
        int count = 1;
        for(int i = length; i <= array.length; i += length) {
            int currentLength = remainingLengthCheck(array.length, i, length);
            current = i < array.length ? initArray(array, currentLength, i) : new byte[currentLength];
            if (currentLength < length && currentLength > 0) {
                arrRLE[indexRLE++] = GetControlByte(currentLength, 1);
                for (int j = 0; j < currentLength; j++) {
                    arrRLE[indexRLE++] = (current[j]);
                }
            } else {
                if (!compare(current, previous) || i == array.length) {
                    arrRLE[indexRLE++] = GetControlByte(length, count);
                    for (int j = 0; j < length; j++) {
                        arrRLE[indexRLE++] = (previous[j]);
                    }
                    count = 1;
                } else {
                    count++;
                }
                previous = current;
            }
        }
        rleSize = indexRLE;
        return arrRLE;
    }

    private int remainingLengthCheck(int generalLength, int readLength, int length) {
        if(generalLength - readLength < length) {
            return generalLength - readLength;
        } else {
            return length;
        }
    }

    private byte hexInByte(String hexByte) {
        return (byte) ((Character.digit(hexByte.charAt(0), 16) << 4) + Character.digit(hexByte.charAt(1), 16));
    }

    /*private byte[] hexStringToByteArray(String s) {
        int length = s.length();
        byte[] byteArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            String byteStr = s.substring(i, i + 2);
            byteArr[i / 2] = hexInByte(byteStr);
        }
        return byteArr;
    }*/
    private byte GetControlByte (int length, int count) {
        String controlByteStr = String.valueOf(length) + getHexNumber(count);
        return hexInByte(controlByteStr);
    }

    private byte[] initArray(byte[] array, int length, int position) {
        byte[] newArray = new byte[length];
        int j = 0;
        for(int i = position ; i < position + length; i++) {
            newArray[j++] = array[i];
        }
        return newArray;
    }

    private String getHexNumber(int value) {
        String hex[] = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        return hex[value];
    }

    private boolean compare(byte[] array1, byte[] array2) {
        if(array1.length != array2.length) {
            return false;
        }
        for(int i = 0; i < array1.length; i++) {
            if(array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    /*private byte[] ByteListInByteArr(ArrayList<Byte> arrayList) {
        byte[] array = new byte[arrayList.size()];
        for(int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }*/

    /*void PrintBytes() {
        System.out.println("Исходная последовательность");
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(String.format("%02x ", bytes[i]));
        }
        System.out.println(' ');
    }

    void PrintRle() {
        System.out.println("Сжатая последовательность");
        for (int i = 0; i < rleSize; i++) {
            System.out.print(String.format("%02x ", rle[i]));
        }
        System.out.println(' ');
    }

    void PrintOriginalSequence() {
        System.out.println("Исходная последовательность из сжатой");
        for (int i = 0; i < original.length; i++) {
            System.out.print(String.format("%02x ", original[i]));
        }
        System.out.println(' ');
    }*/

    public byte[] GetOriginalSequence (byte[] rle, int size) {
        byte[] originalSequence = new byte[size];
        int index = 0;
        for(int i = 0; i < rleSize; ) {
            String controlByte = String.format("%02x", rle[i++]);
            int lengthSequence = hexInInt(controlByte.charAt(0));
            int count = hexInInt(controlByte.charAt(1));
            for(int j = 0; j < count; j++) {
                for(int k = 0; k < lengthSequence; k++) {
                    originalSequence[index++] = rle[i + k];
                }
            }
            i += lengthSequence;
        }
        return originalSequence;
    }

    private int arraySearch (String[] arr, char value) {
        for(int i = 0; i < arr.length; i++) {
            if (arr[i].equals("" + value)) {
                return i;
            }
        }
        return -1;
    }


    private int hexInInt (char value) {
        String hex[] = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        return arraySearch(hex, value);
    }

}
