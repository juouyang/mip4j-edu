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
package mip4j.tutorial.ex07;

import java.io.IOException;
import java.util.List;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.net.ConfigurationException;
import org.dcm4che2.tool.dcmqr.DcmQR;
import org.dcm4che2.tool.dcmqr.DcmQR.QueryRetrieveLevel;

//C-FIND
//dcmqr CONQUESTSRV1@127.0.0.1:5678 -qPatientName=HEAD*
public class Query {

	public static void main(String[] args) throws IOException,
			InterruptedException, ConfigurationException {
		DcmQR dcmqr = new DcmQR("DCMQR");
		dcmqr.setCalledAET("CONQUESTSRV1", false);
		dcmqr.setRemoteHost("localhost");
		dcmqr.setRemotePort(5678);
		dcmqr.setPackPDV(true);
		dcmqr.setTcpNoDelay(true);
		dcmqr.setMaxOpsInvoked(1);
		dcmqr.setMaxOpsPerformed(0);
		dcmqr.setCFind(true);
		dcmqr.setCGet(false);
		dcmqr.setQueryLevel(QueryRetrieveLevel.PATIENT);
		// dcmqr.setQueryLevel(QueryRetrieveLevel.STUDY);
		// dcmqr.setQueryLevel(QueryRetrieveLevel.SERIES);
		// dcmqr.setQueryLevel(QueryRetrieveLevel.IMAGE);
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
		}
		dcmqr.close();
		dcmqr.stop();
	}

}
