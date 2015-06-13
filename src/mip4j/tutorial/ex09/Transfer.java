/*******************************************************************************
 * Copyright (c) 2014 Ju Ouyang (juouyang@gmail.com).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Ju Ouyang (juouyang@gmail.com) - initial API and implementation
 ******************************************************************************/
package mip4j.tutorial.ex09;

import java.io.IOException;
import java.util.List;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.net.ConfigurationException;
import org.dcm4che2.tool.dcmqr.DcmQR;
import org.dcm4che2.tool.dcmqr.DcmQR.QueryRetrieveLevel;

// C-MOVE
// dcmqr -L DCMRCV:11106 CONQUESTSRV1@localhost:5678 -cmove DCMRCV -qPatientName=HEAD* -cstore CT -cstoredest R:/tmp
public class Transfer {

	public static void main(String[] args) throws IOException,
			ConfigurationException, InterruptedException {
		DcmQR dcmqr = new DcmQR("DCMQR");
		dcmqr.setCalledAET("CONQUESTSRV1", false); // ConQuest Server does not
													// support C-GET
		dcmqr.setRemoteHost("localhost");
		dcmqr.setRemotePort(5678);
		dcmqr.setCalling("DCMRCV"); // Set this AE in ConQuest Server
		dcmqr.setLocalPort(11106); // Set this AE in ConQuest Server
		dcmqr.setPackPDV(true);
		dcmqr.setTcpNoDelay(true);
		dcmqr.setMaxOpsInvoked(1);
		dcmqr.setMaxOpsPerformed(0);
		dcmqr.addStoreTransferCapability(DcmQR.CUID.valueOf("CT").uid,
				DcmQR.DEF_TS);
		dcmqr.setStoreDestination("R:/tmp");
		dcmqr.setCFind(true);
		dcmqr.setCGet(false);
		dcmqr.setMoveDest("DCMRCV"); // Set this AE in ConQuest Server (same as
										// above)
		dcmqr.setQueryLevel(QueryRetrieveLevel.STUDY);
		dcmqr.addDefReturnKeys();
		dcmqr.configureTransferCapability(false);

		dcmqr.addMatchingKey(Tag.toTagPath("PatientName"), "HEAD*");

		dcmqr.start();
		dcmqr.open();
		List<DicomObject> result;
		if (dcmqr.isCFind()) {
			result = dcmqr.query();

			for (DicomObject dcmObj : result)
				System.out.println(dcmObj);

			if (dcmqr.isCMove()) {
				dcmqr.move(result);

				for (DicomObject dcmObj : result)
					System.out.println(dcmObj);
			}
		}
		dcmqr.close();
		dcmqr.stop();
	}

}
